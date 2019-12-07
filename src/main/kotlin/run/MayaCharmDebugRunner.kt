package run

import resources.MayaNotifications
import settings.ApplicationSettings

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RuntimeConfigurationException
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.process.impl.ProcessListUtil
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugProcessStarter
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import com.jetbrains.python.debugger.PyDebugRunner
import com.jetbrains.python.debugger.PyLocalPositionConverter
import com.jetbrains.python.debugger.attach.PyAttachToProcessCommandLineState
import com.jetbrains.python.sdk.PythonSdkType
import java.net.ServerSocket

class MayaCharmDebugRunner : PyDebugRunner() {
    override fun getRunnerId(): String {
        return "MayaCharmDebugRunner"
    }

    override fun canRun(executorId: String, runProfile: RunProfile): Boolean {
        val runConfig = runProfile as? MayaCharmRunConfiguration ?: return false

        try {
            runConfig.checkConfiguration()
        } catch (e: RuntimeConfigurationException) {
            return false
        }

        return DefaultDebugExecutor.EXECUTOR_ID == executorId
    }

    override fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        val sdks = ApplicationSettings.INSTANCE.mayaSdkMapping
        val runConfig = environment.runProfile as MayaCharmRunConfiguration
        val sdkInfo = sdks[runConfig.mayaSdkPath] ?: return null

        val process = ProcessListUtil.getProcessList().firstOrNull { it.commandLine.contains(sdkInfo.mayaPath) }
        if (process == null) {
            MayaNotifications.mayaInstanceNotFound(sdkInfo.mayaPath, environment.project)
            return null
        }

        val sdk = PythonSdkType.findSdkByPath(sdkInfo.mayaPyPath)
        if (sdk == null) {
            MayaNotifications.mayaInstanceNotFound(sdkInfo.mayaPath, environment.project)
            return null
        }

        val serverSocket = ServerSocket(0) // port 0 forces the ServerSocket to choose its own free port
        val cliState = PyAttachToProcessCommandLineState.create(environment.project, sdk.homePath!!, serverSocket.localPort, process.pid)
        val executionResult = cliState.execute(environment.executor, this)

        val session = XDebuggerManager.getInstance(environment.project).startSession(environment, object : XDebugProcessStarter() {
            override fun start(session: XDebugSession): XDebugProcess {
                val debugProcess = MayaCharmDebugProcess(
                        session,
                        serverSocket,
                        executionResult.executionConsole,
                        executionResult.processHandler,
                        runConfig,
                        process.pid
                )
                debugProcess.positionConverter = PyLocalPositionConverter()
                createConsoleCommunicationAndSetupActions(environment.project, executionResult, debugProcess, session)
                return debugProcess
            }
        })

        return session.runContentDescriptor
    }
}
