package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.xdebugger.XDebugSession;
import com.jetbrains.python.debugger.PyDebugProcess;
import com.jetbrains.python.debugger.remote.PyRemoteDebugRunner;
import com.jetbrains.python.run.PythonCommandLineState;
import org.jetbrains.annotations.NotNull;

import java.net.ServerSocket;

public class MayaCharmDebugRunner extends PyRemoteDebugRunner {
    @Override
    protected XDebugSession createSession(@NotNull RunProfileState runProfileState, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return super.createSession(runProfileState, executionEnvironment);
    }

    @Override
    public boolean canRun(@NotNull String s, @NotNull RunProfile runProfile) {
        return true;
    }

    @NotNull
    @Override
    protected PyDebugProcess createDebugProcess(@NotNull XDebugSession xDebugSession, ServerSocket serverSocket, ExecutionResult executionResult, PythonCommandLineState pythonCommandLineState) {
        System.out.print("Create Debug Process");
        return super.createDebugProcess(xDebugSession, serverSocket, executionResult, pythonCommandLineState);
    }
}
