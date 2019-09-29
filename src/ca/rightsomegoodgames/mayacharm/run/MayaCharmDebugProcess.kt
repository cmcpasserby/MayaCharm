package ca.rightsomegoodgames.mayacharm.run

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.resources.PythonStrings
import ca.rightsomegoodgames.mayacharm.settings.ApplicationSettings
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.python.debugger.PyDebugProcess
import java.net.ServerSocket
import java.text.MessageFormat

class MayaCharmDebugProcess(session: XDebugSession,
                            private val serverSocket: ServerSocket,
                            executionConsole: ExecutionConsole,
                            processHandler: ProcessHandler?,
                            private val runConfig: MayaCharmRunConfiguration?,
                            private val pid: Int)
                            : PyDebugProcess(session, serverSocket, executionConsole, processHandler, false) {

    override fun getConnectionMessage(): String {
        return "Attaching to Maya process with a PID=${pid}"
    }

    override fun getConnectionTitle(): String {
        return "Attaching Debugger to Maya"
    }

    override fun afterConnect() {
        printToConsole("after connect", ConsoleViewContentType.SYSTEM_OUTPUT)

        runConfig ?: return

        val sdkSettings = ApplicationSettings.INSTANCE.mayaSdkMapping[runConfig.mayaSdkPath]
        val maya = MayaCommandInterface(sdkSettings!!.port)

        if (isConnected) {
//            val attachLocalScript = MessageFormat.format(
//                    PythonStrings.INSTANCE.SETTRACE,
//                    "localhost",
//                    serverSocket.localPort,
//                    "True", // Suspend process
//                    "True" // redirect output
//            )
//            maya.sendCodeToMaya(attachLocalScript)

            when (runConfig.executionType) {
                ExecutionType.FILE -> maya.sendFileToMaya(runConfig.scriptFilePath)
                ExecutionType.CODE -> maya.sendCodeToMaya(runConfig.scriptCodeText)
            }
        }
        else {
            printToConsole("FAILED to connect to Maya", ConsoleViewContentType.SYSTEM_OUTPUT)
        }
    }
}
