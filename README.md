# comp2021-oj

## File Structure

| Folder & Main File               | Description                                          |
| -------------------------------- | ---------------------------------------------------- |
| [`app`/](./app)                  | A reference implementation of the CVFS               |
| [`cut/`](./cut)                  | To store code under test (CUT) for grading           |
| [`oj/`](./oj/)                   | The main file for grading judge                      |
| [`res/`](./res/)                 | To store results files                               |
| [`submissions/`](./submissions/) | Student submissions files (like .zip files)          |
| [`grading.py`](./grading.py)     | The grading script will be executed in the container |
| [`main.py`](./main.py)           | The main file to run the online judge                |

## Prerequisite

- Python 3.11+
- Docker

## Quick Start

### Start the container
```bash
docker compose up -d
```
And the container will be named `comp2021-oj`.

### Optional: Enter the container
```bash
docker compose exec dev bash
```

### Optional: Verify the installation
```bash
java --version
git --version
```
And then exit the container, and go back to the host machine.
## Prepare for grading

**Put student programs into `submissions/`** on the **host machine**.

Note: the file in folder `cut/` will be deleted routinely during grading. Please **DO NOT** put any important files into the folder `cut/`.

## Run the online judge
```bash
python main.py
```
