package debugattach

import MayaBundle as Loc
import run.MayaCharmDebugProcess
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.IconLoader
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugProcessStarter
import com.intellij.xdebugger.XDebugSession
import com.intellij.xdebugger.XDebuggerManager
import com.jetbrains.python.debugger.PyDebugRunner
import com.jetbrains.python.debugger.PyLocalPositionConverter
import com.jetbrains.python.debugger.attach.PyAttachToProcessCommandLineState
import com.jetbrains.python.debugger.attach.PyAttachToProcessDebugRunner
import java.io.IOException
import java.net.ServerSocket

class MayaAttachToProcessDebugRunner(
        private val project: Project,
        private val pid: Int,
        private val sdkPath: String?) : PyAttachToProcessDebugRunner(project, pid, sdkPath) {

    override fun launch(): XDebugSession? {
        FileDocumentManager.getInstance().saveAllDocuments()
        return launchRemoteDebugServer()
    }

    private fun getDebuggerSocket(): ServerSocket? {
        var portSocket: ServerSocket? = null

        try {
            portSocket = ServerSocket(0)
        } catch (e: IOException) {
            e.printStackTrace()
            Messages.showErrorDialog(
                    Loc.message("mayacharm.debugattachproc.FailedFindPort"),
                    Loc.message("mayacharm.debugattachproc.FailedFindPortTitle")
            )
        }
        return portSocket
    }

    private fun launchRemoteDebugServer(): XDebugSession? {
        val serverSocket = getDebuggerSocket() ?: return null
        val state = PyAttachToProcessCommandLineState.create(project, sdkPath!!, serverSocket.localPort, pid)
        val result = state.execute(state.environment.executor, this)

        val icon = IconLoader.getIcon("/icons/MayaCharm_ToolWindow.png")

        return XDebuggerManager.getInstance(project).startSessionAndShowTab(pid.toString(), icon, null, false, object : XDebugProcessStarter() {
            override fun start(dSession: XDebugSession): XDebugProcess {
                val process = MayaCharmDebugProcess(dSession, serverSocket, result.executionConsole, result.processHandler, null, pid)
                process.positionConverter = PyLocalPositionConverter()
                PyDebugRunner.createConsoleCommunicationAndSetupActions(project, result, process, dSession)
                return process
            }
        })
    }
}
