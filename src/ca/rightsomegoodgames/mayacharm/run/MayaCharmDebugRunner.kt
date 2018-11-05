package ca.rightsomegoodgames.mayacharm.run

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.impl.ProcessListUtil
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugProcessStarter
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import com.jetbrains.python.debugger.PyDebugRunner
import com.jetbrains.python.debugger.PyLocalPositionConverter
import com.jetbrains.python.sdk.PythonSdkType
import java.net.ServerSocket

class MayaCharmDebugRunner : PyDebugRunner() {
    override fun getRunnerId(): String {
        return "MayaCharmDebugRunner"
    }

    override fun canRun(s: String, runProfile: RunProfile): Boolean {
        return runProfile is MayaCharmRunConfiguration && s == "Debug"
    }

    override fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        val sdk = PythonSdkType.getAllLocalCPythons().first { it.homePath!!.contains("mayapy.exe") }
        val process = ProcessListUtil.getProcessList().first { it.executableName == "maya.exe" }

        val serverSocket = ServerSocket(0)
        val executionResult = state.execute(environment.executor, this)

        val session = XDebuggerManager.getInstance(environment.project)
            .startSession(
                environment,
                object : XDebugProcessStarter() {
                    override fun start(session: XDebugSession): XDebugProcess {
                        val debugProcess = MayaCharmDebugProcess(
                                session,
                                serverSocket,
                                executionResult!!.executionConsole,
                                executionResult.processHandler,
                                false,
                                sdk,
                                process
                        )

                        debugProcess.positionConverter = PyLocalPositionConverter()
                        createConsoleCommunicationAndSetupActions(environment.project, executionResult, debugProcess, session)

                        return debugProcess
                    }
                }
            )

        // TODO: good class to look at for a code example
//        val attachRunner = PyAttachToProcessDebugRunner(environment.project, process.pid, result.homePath)
//        attachRunner.launch()

        return session.runContentDescriptor
    }
}
