package ca.rightsomegoodgames.mayacharm.run

import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.process.ProcessInfo
import com.intellij.execution.ui.ConsoleViewContentType
import com.intellij.execution.ui.ExecutionConsole
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.xdebugger.XDebugSession
import com.jetbrains.python.debugger.PyDebugProcess
import java.net.ServerSocket

class MayaCharmDebugProcess(session: XDebugSession,
                            serverSocket: ServerSocket,
                            executionConsole: ExecutionConsole,
                            processHandler: ProcessHandler?,
                            multiProcess: Boolean,
                            private val sdk: Sdk,
                            private val process: ProcessInfo
                            )
                            : PyDebugProcess(session,
                                serverSocket,
                                executionConsole,
                                processHandler,
                                multiProcess) {

    override fun printToConsole(text: String?, contentType: ConsoleViewContentType?) {
    }

    override fun detachDebuggedProcess() {
        handleStop()
    }

    override fun getConnectionMessage(): String {
        return "Attaching to a process with a PID=${process.pid}" // TODO: pass in from runner in constructor
    }

    override fun getConnectionTitle(): String {
        return "Attaching Debugger"
    }

    override fun afterConnect() {
        super.afterConnect() // TODO: once the debugger is knowen to be attached we can execute our maya code
    }
}
