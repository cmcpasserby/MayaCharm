package ca.rightsomegoodgames.mayacharm.run

import ca.rightsomegoodgames.mayacharm.MayaBundle as Loc
import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.settings.ApplicationSettings
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.python.debugger.PyDebugProcess
import java.net.ServerSocket

class MayaCharmDebugProcess(session: XDebugSession,
                            private val serverSocket: ServerSocket,
                            executionConsole: ExecutionConsole,
                            processHandler: ProcessHandler?,
                            private val runConfig: MayaCharmRunConfiguration?,
                            private val pid: Int)
                            : PyDebugProcess(session, serverSocket, executionConsole, processHandler, false) {

    override fun getConnectionMessage(): String {
        return Loc.message("mayacharm.debugproc.ConnectionMessage", pid)
    }

    override fun getConnectionTitle(): String {
        return Loc.message("mayacharm.debugproc.ConnectionTitle")
    }

    override fun afterConnect() {
        runConfig ?: return

        val sdkSettings = ApplicationSettings.INSTANCE.mayaSdkMapping[runConfig.mayaSdkPath]
        val maya = MayaCommandInterface(sdkSettings!!.port)

        if (isConnected) {
            when (runConfig.executionType) {
                ExecutionType.FILE -> maya.sendFileToMaya(runConfig.scriptFilePath)
                ExecutionType.CODE -> maya.sendCodeToMaya(runConfig.scriptCodeText)
            }
        }
        else {
            printToConsole(Loc.message("mayacharm.debugproc.FailedToConnect"), ConsoleViewContentType.SYSTEM_OUTPUT)
        }
    }
}
