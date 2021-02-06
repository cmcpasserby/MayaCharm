package debugattach

import MayaBundle as Loc
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.python.debugger.PyDebugProcess
import mayacomms.MayaCommandInterface
import resources.PythonStrings
import settings.ApplicationSettings
import java.net.ServerSocket
import java.text.MessageFormat

class MayaCharmDebugAttachProcess(
    session: XDebugSession,
    private val serverSocket: ServerSocket,
    executionConsole: ExecutionConsole,
    processHandler: ProcessHandler?,
    sdkSettings: ApplicationSettings.SdkInfo,
    private val pid: Int
    ) : PyDebugProcess(session, serverSocket, executionConsole, processHandler, false) {

    private val mayaCommand = MayaCommandInterface(sdkSettings.port)

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

        val connectionString = MessageFormat.format(
            PythonStrings.SETTRACE.message,
            "localhost",
            serverSocket.localPort,
            "False",
            "True"
        )
        mayaCommand.sendCodeToMaya(connectionString)
    }

    override fun disconnect() {
        super.disconnect()
        mayaCommand.sendCodeToMaya(PythonStrings.STOPTRACE.message)
    }
}