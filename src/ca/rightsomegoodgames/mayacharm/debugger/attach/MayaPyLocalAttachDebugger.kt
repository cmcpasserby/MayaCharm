package ca.rightsomegoodgames.mayacharm.debugger.attach

import com.intellij.execution.process.ProcessInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.xdebugger.attach.XLocalAttachDebugger
import com.jetbrains.python.debugger.attach.PyAttachToProcessDebugRunner
import com.jetbrains.python.sdk.PythonSdkType

class MayaPyLocalAttachDebugger: XLocalAttachDebugger {
    private val mySdkHome: String?
    private val myName: String

    constructor(sdk: Sdk) {
        mySdkHome = sdk.homePath
        myName = PythonSdkType.getInstance().getVersionString(sdk) + "($mySdkHome)"
    }

    constructor(sdkHome: String) {
        mySdkHome = sdkHome
        myName = "Python Debugger"
    }

    override fun getDebuggerDisplayName(): String {
        return myName
    }

    override fun attachDebugSession(project: Project, processInfo: ProcessInfo) {
        val runner = PyAttachToProcessDebugRunner(project, processInfo.pid, mySdkHome)
        runner.launch()
    }
}
