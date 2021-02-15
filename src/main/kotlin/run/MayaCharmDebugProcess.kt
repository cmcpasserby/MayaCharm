package run

import MayaBundle as Loc
import mayacomms.MayaCommandInterface
import settings.ApplicationSettings
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.python.debugger.PyDebugProcess
import resources.PythonStrings
import java.net.ServerSocket

class MayaCharmDebugProcess(
    session: XDebugSession,
    serverSocket: ServerSocket,
    executionConsole: ExecutionConsole,
    processHandler: ProcessHandler?,
    private val runConfig: MayaCharmRunConfiguration?,
    private val pid: Int
) : PyDebugProcess(session, serverSocket, executionConsole, processHandler, false) {

    private val mayaCommand = runConfig?.mayaSdkPath?.let {
        val port = ApplicationSettings.INSTANCE.mayaSdkMapping[it]?.port ?: return@let null
        MayaCommandInterface(port)
    }

    override fun getConnectionMessage(): String {
        return Loc.message("mayacharm.debugproc.ConnectionMessage", pid.toString())
    }

    override fun getConnectionTitle(): String {
        return Loc.message("mayacharm.debugproc.ConnectionTitle")
    }

    override fun afterConnect() {
        if (!isConnected) {
            printToConsole(Loc.message("mayacharm.debugproc.FailedToConnect"), ConsoleViewContentType.SYSTEM_OUTPUT)
            return
        }

        runConfig ?: return
        mayaCommand ?: return

        when (runConfig.executionType) {
            ExecutionType.FILE -> mayaCommand.sendFileToMaya(runConfig.scriptFilePath)
            ExecutionType.CODE -> mayaCommand.sendCodeToMaya(runConfig.scriptCodeText)
        }
    }

    override fun disconnect() {
        super.disconnect()
        mayaCommand?.sendCodeToMaya(PythonStrings.STOPTRACE.message)
    }
}
