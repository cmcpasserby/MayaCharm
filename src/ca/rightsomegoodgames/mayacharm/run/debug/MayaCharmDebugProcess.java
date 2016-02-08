package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.xdebugger.XDebugSession;
import com.jetbrains.python.debugger.PyRemoteDebugProcess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.ServerSocket;

public class MayaCharmDebugProcess extends PyRemoteDebugProcess {
    public MayaCharmDebugProcess(@NotNull XDebugSession xDebugSession, @NotNull ServerSocket serverSocket, @NotNull ExecutionConsole executionConsole, @Nullable ProcessHandler processHandler, @Nullable String s) {
        super(xDebugSession, serverSocket, executionConsole, processHandler, s);
    }

    @Override
    public void waitForNextConnection() {
        super.waitForNextConnection();
        System.out.print("MayaCharm Connected");
    }

    @Override
    protected void afterConnect() {
        super.afterConnect();
        System.out.print("MayaCharm Connected");
    }

    @Override
    public void stop() {
        super.stop();
        System.out.print("MayaCharm Connected");
    }
}
