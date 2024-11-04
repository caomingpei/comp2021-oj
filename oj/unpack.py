import os
import patoolib
import argparse


def extract_auto(archive_path: str, extract_path=None):
    """
    Automatically detect and extract archive format
    Supports: zip, 7z, rar, tar, gz, bz2, xz and many more
    """
    try:
        # If no extract path provided, use same directory as archive
        if extract_path is None:
            extract_path = os.path.dirname(archive_path)

        # Create extract directory if it doesn't exist
        os.makedirs(extract_path, exist_ok=True)

        # Auto-detect and extract
        patoolib.extract_archive(archive_path, outdir=extract_path)
        return True

    except Exception as e:
        print(f"Error extracting {archive_path}: {str(e)}")
        return False


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("-i", "--input", type=str)
    parser.add_argument("-o", "--output", type=str)
    args = parser.parse_args()
    extract_auto(args.input, args.output)
