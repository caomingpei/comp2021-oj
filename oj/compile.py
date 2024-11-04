import xml.etree.ElementTree as ET
import os
import subprocess
import sys
from pathlib import Path


class ImlBuilder:
    def __init__(self, iml_path):
        self.iml_path = iml_path
        self.project_root = os.path.dirname(os.path.abspath(iml_path))
        self.output_dir = os.path.join(self.project_root, "out")
        self.sources = []
        self.test_sources = []
        self.libraries = set()
        self.parse_iml()

    def parse_iml(self):
        tree = ET.parse(self.iml_path)
        root = tree.getroot()

        for content in root.findall(".//content"):
            for source_folder in content.findall("sourceFolder"):
                url = source_folder.get("url")
                path = url.replace("file://$MODULE_DIR$", self.project_root)
                is_test = source_folder.get("isTestSource") == "true"
                if is_test:
                    self.test_sources.append(path)
                else:
                    self.sources.append(path)

        for order_entry in root.findall(".//orderEntry[@type='module-library']"):
            library = order_entry.find(".//library")
            if library is not None:
                for classes in library.findall(".//CLASSES/root"):
                    jar_url = classes.get("url")
                    if jar_url:
                        jar_path = jar_url.replace("jar://", "").replace("!/", "")
                        jar_path = jar_path.replace(
                            "$APPLICATION_HOME_DIR$",
                            os.getenv("IDEA_HOME", "/usr/local/intellij-idea"),
                        )
                        self.libraries.add(jar_path)

    def find_java_files(self, directories):
        java_files = []
        for directory in directories:
            for root, _, files in os.walk(directory):
                for file in files:
                    if file.endswith(".java"):
                        java_files.append(os.path.join(root, file))
        return java_files

    def create_classpath(self):
        lib_dir = os.path.join(self.project_root, "lib")
        project_jars = []
        if os.path.exists(lib_dir):
            for file in os.listdir(lib_dir):
                if file.endswith(".jar"):
                    project_jars.append(os.path.join(lib_dir, file))

        all_libs = set(project_jars) | self.libraries
        return ":".join(all_libs)

    def compile(self):
        try:
            os.makedirs(self.output_dir, exist_ok=True)

            if self.sources:
                print("Compiling source files...")
                source_files = self.find_java_files(self.sources)
                if source_files:
                    classpath = self.create_classpath()
                    cmd = ["javac", "-d", self.output_dir]
                    if classpath:
                        cmd.extend(["-cp", classpath])
                    cmd.extend(source_files)
                    subprocess.run(cmd, check=True)

            if self.test_sources:
                print("Compiling test files...")
                test_files = self.find_java_files(self.test_sources)
                if test_files:
                    classpath = f"{self.output_dir}:{self.create_classpath()}"
                    cmd = ["javac", "-d", self.output_dir, "-cp", classpath]
                    cmd.extend(test_files)
                    subprocess.run(cmd, check=True)

            print("Compilation successful!")
            return True

        except subprocess.CalledProcessError as e:
            print(f"Compilation failed: {e}")
            return False
        except Exception as e:
            print(f"Error during compilation: {e}")
            return False

    def find_test_classes(self):
        test_classes = set()

        for test_src in self.test_sources:
            for root, _, files in os.walk(test_src):
                for file in files:
                    if file.endswith("Test.java"):
                        rel_path = os.path.relpath(os.path.join(root, file), test_src)
                        class_name = rel_path[:-5].replace(os.path.sep, ".")
                        test_classes.add(class_name)

        return sorted(test_classes)

    def run_tests(self):
        if not self.test_sources:
            print("No test sources found")
            return True

        try:
            print("Running tests...")
            lib_classpath = os.path.join(self.project_root, "lib", "*")
            classpath = f"{self.output_dir}:{lib_classpath}:{self.create_classpath()}"

            test_classes = self.find_test_classes()

            if test_classes:
                print(f"Found test classes: {test_classes}")
                cmd = ["java", "-cp", classpath, "org.junit.runner.JUnitCore"]
                cmd.extend(test_classes)

                process = subprocess.run(
                    cmd,
                    check=True,
                    text=True,
                    stdout=subprocess.PIPE,
                    stderr=subprocess.PIPE,
                )

                print(process.stdout)
                if process.stderr:
                    print("Errors:", process.stderr)

                print("All tests passed!")
                return True
            else:
                print("No test classes found")
                return True

        except subprocess.CalledProcessError as e:
            print(f"Tests failed with exit code {e.returncode}")
            if e.stdout:
                print("Output:", e.stdout)
            if e.stderr:
                print("Errors:", e.stderr)
            return False
        except Exception as e:
            print(f"Error running tests: {e}")
            return False


def find_src_path(start_dir: str):
    source_code_path = start_dir
    for name in os.listdir(source_code_path):
        cur_path = os.path.join(source_code_path, name)
        if os.path.isdir(cur_path):
            source_code_path = cur_path  # GroupID
            break

    source_code_path = os.path.join(source_code_path, "SourceCode")
    for name in os.listdir(source_code_path):
        if os.path.isfile(os.path.join(source_code_path, name)) and name.endswith(
            ".iml"
        ):
            return source_code_path
    dirs = [
        os.path.join(source_code_path, name)
        for name in os.listdir(source_code_path)
        if os.path.isdir(os.path.join(source_code_path, name))
    ]
    if len(dirs) == 1:
        return os.path.join(source_code_path, dirs[0])
    return source_code_path


def run() -> dict[str, bool]:
    start_dir = "/cut"
    src_path = Path(find_src_path(start_dir))
    print(src_path)

    iml_files = list(src_path.glob("*.iml"))
    if not iml_files:
        print("No .iml file found in current directory")
        return 1

    builder = ImlBuilder(str(iml_files[0]))

    compile_result = True if builder.compile() else False
    test_result = True if builder.run_tests() else False

    return {"compile": compile_result, "test": test_result, "src_path": str(src_path)}
