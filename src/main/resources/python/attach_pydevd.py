from __future__ import print_function
import sys
import os
import socket


def process_command_line(argv):
    setup = {}
    setup['port'] = 5678  # Default port for PyDev remote debugger
    setup['pid'] = 0
    setup['host'] = '127.0.0.1'
    setup['protocol'] = ''

    i = 0
    while i < len(argv):
        if argv[i] == '--port':
            del argv[i]
            setup['port'] = int(argv[i])
            del argv[i]

        elif argv[i] == '--pid':
            del argv[i]
            setup['pid'] = int(argv[i])
            del argv[i]

        elif argv[i] == '--host':
            del argv[i]
            setup['host'] = argv[i]
            del argv[i]

        elif argv[i] == '--protocol':
            del argv[i]
            setup['protocol'] = argv[i]
            del argv[i]

        elif argv[i] == '--mcPort':
            del argv[i]
            setup['mcPort'] = int(argv[i])
            del argv[i]

        elif argv[i] == '--pydevPath':
            del argv[i]
            setup['pydevPath'] = argv[i]
            del argv[i]

    if not setup['pid']:
        sys.stderr.write('Expected --pid to be passed.\n')
        sys.exit(1)
    return setup


def send_command(port, message):
    client = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
    client.connect(("localhost", port))
    client.sendall('python("' + message.replace(r'"', r'\"') + '")')
    data = client.recv(1024)
    if data and not data.startswith("None"):
        print(data)
    client.close()


def main(setup):
    if sys.platform == 'win32':
        setup['pythonpath'] = setup['pydevPath'].replace("\\", "/")
        setup['pythonpath2'] = os.path.dirname(__file__).replace('\\', '/')
        python_code = '''import sys;
sys.path.append("%(pythonpath)s");
sys.path.append("%(pythonpath2)s");
import attach_script;
attach_script.attach(%(port)s, "%(host)s");
'''.replace('\r\n', '').replace('\r', '').replace('\n', '')
    else:
        setup['pythonpath'] = setup['pydevPath']
        setup['pythonpath2'] = os.path.dirname(__file__)
        # We have to pass it a bit differently for gdb
        python_code = '''import sys;
sys.path.append("%(pythonpath)s");
sys.path.append("%(pythonpath2)s");
import attach_script;
attach_script.attach(port=%(port)s, host="%(host)s");
'''.replace('\r\n', '').replace('\r', '').replace('\n', '')

    python_code = python_code % setup
    send_command(setup['mcPort'], python_code)


if __name__ == '__main__':
    main(process_command_line(sys.argv[1:]))
