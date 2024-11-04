# This file is used to grade single submission
import os
import shutil
from config import APP_DIR, CONTAINER_NAME
import docker
import platform
import subprocess
from pathlib import Path


def clean_dir(directory: str):
    system = platform.system().lower()
    if system == "windows":
        for item in os.listdir(directory):
            item_path = os.path.join(directory, item)
            try:
                if os.path.isfile(item_path):
                    subprocess.run(
                        ["del", "/f", "/q", item_path], shell=True, check=True
                    )
                else:
                    subprocess.run(
                        ["rd", "/s", "/q", item_path], shell=True, check=True
                    )
            except subprocess.CalledProcessError as e:
                print(f"Error removing {item_path}: {e}")

    else:
        for item in os.listdir(directory):
            item_path = os.path.join(directory, item)
            try:
                subprocess.run(["sudo", "rm", "-rf", item_path], check=True)
            except subprocess.CalledProcessError as e:
                print(f"Error removing {item_path}: {e}")


def wait_for_container(container):
    os.system("sync")
    container.exec_run("sync", user="root")
    container.exec_run("mount -o remount /app", user="root")


def judge(zip_file_path: str):
    zip_file = os.path.basename(zip_file_path)
    clean_dir(APP_DIR)
    shutil.copy2(zip_file_path, os.path.join(APP_DIR, zip_file))

    client = docker.from_env()
    container = client.containers.get(CONTAINER_NAME)
    print(container.status)

    wait_for_container(container)

    result = container.exec_run(
        f"python3 /oj/unpack.py -i /app/{zip_file} -o /app",
        workdir="/",
        privileged=True,
        tty=True,
        user="root",
    )

    zip_name = zip_file.split(".")[0] if zip_file.endswith(".zip") else zip_file
    result = container.exec_run(
        f"python3 /oj/terminal.py -n {zip_name}",
        workdir="/",
        privileged=True,
        tty=True,
        user="root",
    )
    print(result.output.decode("utf-8"))
