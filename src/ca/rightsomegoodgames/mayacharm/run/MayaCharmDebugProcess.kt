package ca.rightsomegoodgames.mayacharm.run

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.settings.ApplicationSettings
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessInfo
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.python.debugger.PyDebugProcess
import java.net.ServerSocket

class MayaCharmDebugProcess(session: XDebugSession,
                            serverSocket: ServerSocket,
                            executionConsole: ExecutionConsole,
                            processHandler: ProcessHandler?,
                            multiProcess: Boolean,
                            private val runConfig: MayaCharmRunConfiguration,
                            private val process: ProcessInfo
                            )
                            : PyDebugProcess(session,
                                serverSocket,
                                executionConsole,
                                processHandler,
                                multiProcess) {

    override fun printToConsole(text: String?, contentType: ConsoleViewContentType?) { }

    override fun getConnectionMessage(): String {
        return "Attaching to Maya process with a PID=${process.pid}"
    }

    override fun getConnectionTitle(): String {
        return "Attaching Debugger to Maya"
    }

    override fun disconnect() {
        super.disconnect()
        Thread.sleep(500)
    }

    override fun afterConnect() {
        super.afterConnect()
        val sdkSettings = ApplicationSettings.getInstance().mayaSdkMapping[runConfig.mayaSdkPath]
        val maya = MayaCommandInterface(sdkSettings!!.port)

        Thread.sleep(500) // Maya does not seem to always be ready on time

        when (runConfig.executionType) {
            ExecutionType.FILE -> maya.sendFileToMaya(runConfig.scriptFilePath)
            ExecutionType.CODE -> maya.sendCodeToMaya(runConfig.scriptCodeText)
        }
    }
}
