package ca.rightsomegoodgames.mayacharm.run.debug;

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommInterface;
import ca.rightsomegoodgames.mayacharm.resources.PythonStrings;
import ca.rightsomegoodgames.mayacharm.settings.MCSettingsProvider;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ExecutionConsole;
import com.intellij.xdebugger.XDebugSession;
import com.jetbrains.python.debugger.PyRemoteDebugProcess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.net.ServerSocket;

public class MayaCharmDebugProcess extends PyRemoteDebugProcess {
    private ServerSocket socket;
    private MayaCommInterface mayaCommInterface;
    private boolean canConnect;

    public MayaCharmDebugProcess(@NotNull XDebugSession xDebugSession, @NotNull ServerSocket serverSocket, @NotNull ExecutionConsole executionConsole, @Nullable ProcessHandler processHandler, @Nullable String s) {
        super(xDebugSession, serverSocket, executionConsole, processHandler, s);
        socket = serverSocket;
    }

    @Override
    public void sessionInitialized() {
        super.sessionInitialized();
        canConnect = true;
    }

    @Override
    protected void beforeConnect() {
        super.beforeConnect();
        if (canConnect) {
            MCSettingsProvider mcSettingsProvider = MCSettingsProvider.getInstance(getProject());
            if (mcSettingsProvider != null) {
                mayaCommInterface = new MayaCommInterface(mcSettingsProvider.getHost(), mcSettingsProvider.getPort());
            }
            if (mayaCommInterface != null) {
                mayaCommInterface.connectMayaLog();
                mayaCommInterface.sendCodeToMaya(String.format(PythonStrings.SETTRACE, socket.getLocalPort()));
            }
        }
    }

    @Override
    public void stop() {
        canConnect = false;
        if (mayaCommInterface != null) {
            mayaCommInterface.sendCodeToMaya(PythonStrings.STOPTRACE);
        }
        super.stop();
    }

    @Override
    protected void disconnect() {
        canConnect = false;
        super.disconnect();
    }
}
