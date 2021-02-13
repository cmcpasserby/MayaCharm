from __future__ import print_function


def attach(port, host):
    try:
        import pydevd
        pydevd.stoptrace()  # I.e.: disconnect if already connected
        pydevd.settrace(
            port=port,
            host=host,
            stdoutToServer=True,
            stderrToServer=True,
            overwrite_prev_trace=True,
            suspend=False,
            trace_only_current_thread=False,
            patch_multiprocessing=False,
        )
        print("successfully attached to maya")
    except:
        import traceback
        traceback.print_exc()
