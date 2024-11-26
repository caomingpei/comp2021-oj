from grading import judge
import os
import logging
import shutil
import tempfile

logging.basicConfig(level=logging.INFO, format="%(asctime)s - %(levelname)s - %(message)s")

def create_zip_with_sourcecode(src_dir, group_name):
    with tempfile.TemporaryDirectory() as temp_dir:
        # Group_{group_name}/SourceCode/
        group_dir = os.path.join(temp_dir, f"Group_{group_name}")
        os.makedirs(group_dir)

        source_code_dir = os.path.join(group_dir, "SourceCode")
        os.makedirs(source_code_dir)
        
        for item in os.listdir(src_dir):
            src_path = os.path.join(src_dir, item)
            dst_path = os.path.join(source_code_dir, item)
            if os.path.isdir(src_path):
                shutil.copytree(src_path, dst_path)
            else:
                shutil.copy2(src_path, dst_path)
        
        zip_name = f"Group_{group_name}.zip"
        shutil.make_archive(
            os.path.splitext(zip_name)[0],
            'zip',                          
            temp_dir
        )
        
        logging.info(f"Created {zip_name}")
        return zip_name

if __name__ == "__main__":
    submissions_dir = "archive"
    
    for item in os.listdir(submissions_dir):
        item_path = os.path.join(submissions_dir, item)
        if not os.path.isdir(item_path):
            continue
            
        logging.info(f"Processing {item}")
        
        # 创建新的zip文件
        zip_file = create_zip_with_sourcecode(item_path, item)