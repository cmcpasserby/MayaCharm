import sys


def setup_pydevd():
    pydev_path = r"%s"
    if pydev_path not in sys.path:
        sys.path.append(pydev_path)


if __name__ == "__main__":
    setup_pydevd()
