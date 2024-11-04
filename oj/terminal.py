import compile
import argparse
import yaml
import time
import sys
import os
import pexpect
import json


def run_java(src_path: str):
    out_path = os.path.join(src_path, "out")
    java_command = f"java -cp {out_path} hk.edu.polyu.comp.comp2021.cvfs.Application"

    child = pexpect.spawn(java_command)
    child.logfile = sys.stdout.buffer

    results = []
    timeout_seconds = 2
    time.sleep(1)
    start_time = time.time()
    initial_output = []
    while True:
        if time.time() - start_time > timeout_seconds:
            # Initial output timeout
            break

        try:
            index = child.expect(["\r\n", pexpect.TIMEOUT], timeout=0.2)
            if index == 0:
                line = child.before.decode("utf-8").strip()
                if line:
                    initial_output.append(line)
            else:
                break
        except pexpect.EOF:
            break

    results.append({"command": "", "output": initial_output})

    # wait for the application to run commands
    time.sleep(0.5)

    with open(os.path.join("/oj", "cases.yaml"), "r", encoding="utf-8") as f:
        cases = yaml.safe_load(f)
    commands = [case["input"] for case in cases["commands"]]

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
    with open(os.path.join("/res", f"{group_name}.json"), "w", encoding="utf-8") as f:
        json.dump(running_results, f, ensure_ascii=False, indent=4)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-n", "--name", type=str, required=True)
    args = parser.parse_args()
    entry(args.name)
