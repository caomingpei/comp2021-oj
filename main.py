from grading import judge
import os
import logging

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

if __name__ == "__main__":
    submissions_dir = "submissions"
    files = [file for file in os.listdir(submissions_dir) if file.endswith(".zip")]
    logging.info(f"Found {len(files)} files in {submissions_dir}")
    auto_graded = 0
    for file in files:
        file_path = os.path.join(submissions_dir, file)
        try:
            judge(file_path)
            logging.info(f"Graded {file}")
            auto_graded += 1
        except Exception as e:
            logging.error(f"Error grading {file}: {e}")
    print(f"Finished {auto_graded} files")
