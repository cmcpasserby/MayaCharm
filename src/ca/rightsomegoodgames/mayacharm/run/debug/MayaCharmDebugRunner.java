package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.ExecutionResult;
import com.intellij.xdebugger.XDebugSession;
import com.jetbrains.python.debugger.PyDebugProcess;
import com.jetbrains.python.debugger.remote.PyRemoteDebugRunner;
import com.jetbrains.python.run.PythonCommandLineState;
import org.jetbrains.annotations.NotNull;

import java.net.ServerSocket;

public class MayaCharmDebugRunner extends PyRemoteDebugRunner {

    @NotNull
    @Override
    protected PyDebugProcess createDebugProcess(@NotNull XDebugSession xDebugSession,
                                                ServerSocket serverSocket,
                                                ExecutionResult executionResult,
                                                PythonCommandLineState pythonCommandLineState) {

        return new MayaCharmDebugProcess(xDebugSession, serverSocket,
                executionResult.getExecutionConsole(), executionResult.getProcessHandler(), "MayaCharmDebug");
    }
}
