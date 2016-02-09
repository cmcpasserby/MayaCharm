package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import com.jetbrains.python.debugger.PyDebugRunner;
import com.jetbrains.python.debugger.remote.PyRemoteDebugRunner;
import com.jetbrains.python.debugger.remote.vfs.PyRemotePositionConverter;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.ServerSocket;


public class MayaCharmDebugRunner extends PyRemoteDebugRunner {
    @NotNull
    @Override
    public String getRunnerId() {
        return "MayaCharmDebugRunner";
    }

    @Override
    public boolean canRun(@NotNull String s, @NotNull RunProfile runProfile) {
        return runProfile instanceof MayaCharmDebugConfig && s.equals("Debug");
    }

    @Override
    protected RunContentDescriptor doExecute(@NotNull RunProfileState runProfileState, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        FileDocumentManager.getInstance().saveAllDocuments();
        final MayaCharmDebugConfig mayaCharmDebugConfig = (MayaCharmDebugConfig) executionEnvironment.getRunProfile();

        final ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(mayaCharmDebugConfig.getPort());
        } catch (IOException e) {
            throw new ExecutionException("Failed to find free socket port", e);
        }

        final ExecutionResult executionResult = runProfileState.execute(executionEnvironment.getExecutor(), this);

        XDebugSession xDebugSession = XDebuggerManager.getInstance(executionEnvironment.getProject()).startSession(executionEnvironment, new XDebugProcessStarter() {
            @NotNull
            @Override
            public XDebugProcess start(@NotNull XDebugSession xDebugSession) throws ExecutionException {
                MayaCharmDebugProcess pyRemoteDebugProcess = new MayaCharmDebugProcess(xDebugSession, serverSocket, executionResult.getExecutionConsole(), executionResult.getProcessHandler(), mayaCharmDebugConfig.getSettraceCall(serverSocket.getLocalPort()));
                pyRemoteDebugProcess.setPositionConverter(new PyRemotePositionConverter(pyRemoteDebugProcess, mayaCharmDebugConfig.getMappingSettings()));
                PyDebugRunner.createConsoleCommunicationAndSetupActions(executionEnvironment.getProject(), executionResult, pyRemoteDebugProcess, xDebugSession);
                return pyRemoteDebugProcess;
            }
        });
        return xDebugSession.getRunContentDescriptor();
    }
}
