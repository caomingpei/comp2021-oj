import compile
import argparse
import subprocess
import time
import sys
import os
import pexpect
import json


def get_initial_output(child, timeout_seconds=3):
    def read_line():
        try:
            index = child.expect(["\r\n", pexpect.TIMEOUT], timeout=0.2)
            if index == 0:
                line = child.before.decode("utf-8").strip()
                return line if line else None
            return None
        except pexpect.EOF:
            return None

    time.sleep(1)
    initial_output = []
    start_time = time.time()

    while time.time() - start_time <= timeout_seconds:
        line = read_line()
        if line is None:
            break
        initial_output.append(line)

    return initial_output


def run_java(src_path: str):
    out_path = os.path.join(src_path, "out")
    java_command = f"java -cp {out_path} hk.edu.polyu.comp.comp2021.cvfs.Application"

    child = pexpect.spawn(java_command)
    child.logfile = sys.stdout.buffer

    results = []
    initial_output = get_initial_output(child)
    results.append({"command": "", "output": initial_output})

    # TODO: read from yaml file
    commands = ["newDisk 1000", "newDoc test html aab", "list"]

    for cmd in commands:
        child.sendline(cmd)
        output_lines = []

        while True:
            try:
                index = child.expect(["\r\n", pexpect.TIMEOUT], timeout=0.5)
                if index == 0:
                    line = child.before.decode("utf-8").strip()
                    if line:
                        output_lines.append(line)
                else:
                    break
            except pexpect.EOF:
                break

        results.append({"command": cmd, "output": output_lines})

    child.sendline("quit")
    try:
        child.expect(pexpect.EOF, timeout=3)
    except pexpect.TIMEOUT:
        child.close()

    return results


def entry(group_name: str):
    compile_running = compile.run()
    print(compile_running)

    compile_result = compile_running["compile"]
    test_result = compile_running["test"]
    src_path = compile_running["src_path"]
    if not compile_result:
        print("Compile failed")
        return

    running_results = run_java(src_path)
    with open(
        os.path.join("/results", f"{group_name}.json"), "w", encoding="utf-8"
    ) as f:
        json.dump(running_results, f, ensure_ascii=False, indent=4)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-n", "--name", type=str, required=True)
    args = parser.parse_args()
    entry(args.name)
