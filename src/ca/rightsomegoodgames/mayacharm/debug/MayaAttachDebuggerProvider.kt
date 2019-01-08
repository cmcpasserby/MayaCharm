package ca.rightsomegoodgames.mayacharm.debug

import ca.rightsomegoodgames.mayacharm.settings.ApplicationSettings
import com.intellij.execution.process.ProcessInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.util.UserDataHolder
import com.intellij.xdebugger.attach.XAttachDebugger
import com.intellij.xdebugger.attach.XAttachDebuggerProvider
import com.intellij.xdebugger.attach.XAttachHost
import com.jetbrains.python.debugger.attach.PyAttachToProcessDebugRunner
import com.jetbrains.python.sdk.PythonSdkType


class MayaAttachDebuggerProvider : XAttachDebuggerProvider {
    override fun getAvailableDebuggers(project: Project, attachHost: XAttachHost, processInfo: ProcessInfo, userData: UserDataHolder): MutableList<XAttachDebugger> {
        val sdks = ApplicationSettings.getInstance().mayaSdkMapping.values

        val commandLine = processInfo.commandLine.removeSurrounding("\"")

        if (!sdks.any { it.mayaPath == commandLine }) {
            return mutableListOf()
        }

        val currentSdk = sdks.firstOrNull { commandLine.startsWith(it.mayaPath) } ?: return mutableListOf()
        return mutableListOf(MayaAttachDebugger(PythonSdkType.findSdkByPath(currentSdk.mayaPyPath)!!))
    }

    override fun isAttachHostApplicable(attachHost: XAttachHost): Boolean {
        return true
    }
}

class MayaAttachDebugger(sdk: Sdk) : XAttachDebugger {
    private val mySdkHome: String? = sdk.homePath
    private val myName: String = "${PythonSdkType.getInstance().getVersionString(sdk)} ($mySdkHome)"

    override fun getDebuggerDisplayName(): String {
        return myName
    }

    override fun attachDebugSession(project: Project, attachHost: XAttachHost, processInfo: ProcessInfo) {
        val runner = PyAttachToProcessDebugRunner(project, processInfo.pid, mySdkHome)
        runner.launch()
    }
}
