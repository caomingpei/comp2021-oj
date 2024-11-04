import os

ROOT_DIR = os.path.dirname(os.path.abspath(__file__))

SUBMISSION_DIR = os.path.join(ROOT_DIR, "submissions")
RES_DIR = os.path.join(ROOT_DIR, "res")
CUT_DIR = os.path.join(ROOT_DIR, "cut")

CONTAINER_NAME = "comp2021-oj"  # Please ensure this is the SAME as the container name in docker-compose.yml


os.makedirs(SUBMISSION_DIR, exist_ok=True)
os.makedirs(RES_DIR, exist_ok=True)
