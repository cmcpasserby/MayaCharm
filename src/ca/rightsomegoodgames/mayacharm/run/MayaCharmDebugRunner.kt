package ca.rightsomegoodgames.mayacharm.run

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.settings.ProjectSettings
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessInfo
import com.intellij.execution.process.impl.ProcessListUtil
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugProcessStarter
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import com.jetbrains.python.debugger.PyDebugProcess
import com.jetbrains.python.debugger.PyDebugRunner
import com.jetbrains.python.debugger.PyLocalPositionConverter
import com.jetbrains.python.debugger.attach.PyAttachToProcessCommandLineState
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
        val sdk = PythonSdkType.getAllLocalCPythons().firstOrNull { it.homePath!!.contains("mayapy.exe") } // TODO: get from user defined list
        val process = ProcessListUtil.getProcessList().firstOrNull { it.executableName == "maya.exe" } // TODO: get from user defined list

        val runConfig = environment.runProfile as MayaCharmRunConfiguration

        val serverSocket = ServerSocket(0)

        if (sdk == null || process == null || sdk.homePath == null) {
            return null
        }

        val cliState = PyAttachToProcessCommandLineState.create(environment.project, sdk.homePath!!, serverSocket.localPort, process.pid)

        val executionResult = cliState.execute(environment.executor, this)

        val session = XDebuggerManager.getInstance(environment.project)
            .startSession(
                environment, object : XDebugProcessStarter() {
                    override fun start(session: XDebugSession): XDebugProcess {
                        val debugProcess = DebugProcess(
                                session,
                                serverSocket,
                                executionResult.executionConsole,
                                executionResult.processHandler,
                                false,
                                environment.project,
                                runConfig,
                                process
                        )
                        debugProcess.positionConverter = PyLocalPositionConverter()
                        createConsoleCommunicationAndSetupActions(environment.project, executionResult, debugProcess, session)
                        return debugProcess
                    }
                }
            )
        return session.runContentDescriptor
    }

    class DebugProcess(session: XDebugSession,
                       serverSocket: ServerSocket,
                       executionConsole: ExecutionConsole,
                       processHandler: ProcessHandler?,
                       multiProcess: Boolean,
                       proj: Project,
                       private val runConfig: MayaCharmRunConfiguration,
                       private val process: ProcessInfo)
        : PyDebugProcess(session, serverSocket, executionConsole, processHandler, multiProcess) {

        private val maya: MayaCommandInterface

        init {
            val settings = ProjectSettings.getInstance(proj)
            maya = MayaCommandInterface(settings.host, settings.port)
        }

        override fun printToConsole(text: String?, contentType: ConsoleViewContentType?) {
        }

        override fun detachDebuggedProcess() {
            handleStop()
        }

        override fun getConnectionMessage(): String {
            return "Attaching to Maya process with PID=${process.pid}"
        }

        override fun getConnectionTitle(): String {
            return "Attaching Debugger to Maya"
        }

        override fun init() {
//            maya.pyDevSetup()
            super.init()
        }

        override fun beforeConnect() {
            super.beforeConnect()
        }

        override fun afterConnect() {
            super.afterConnect()
            FileDocumentManager.getInstance().saveAllDocuments()

            when (runConfig.executionType) {
                ExecutionType.FILE -> maya.sendFileToMaya(runConfig.scriptFilePath)
                ExecutionType.CODE -> maya.sendCodeToMaya(runConfig.scriptCodeText)
            }
        }
    }
}
