package ca.rightsomegoodgames.mayacharm.debugattach

import ca.rightsomegoodgames.mayacharm.settings.ApplicationSettings
import com.intellij.execution.process.ProcessInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.UserDataHolder
import com.intellij.xdebugger.attach.*
import com.jetbrains.python.debugger.attach.PyAttachToProcessDebugRunner
import com.jetbrains.python.sdk.PythonSdkType
import javax.swing.Icon


class MayaAttachDebuggerProvider : XAttachDebuggerProvider {
    override fun getPresentationGroup(): XAttachPresentationGroup<ProcessInfo> {
        return MayaAttachGroup.INSTANCE
    }

    override fun getAvailableDebuggers(project: Project, attachHost: XAttachHost, processInfo: ProcessInfo, userData: UserDataHolder): MutableList<XAttachDebugger> {
        val sdks = ApplicationSettings.getInstance().mayaSdkMapping.values

        if (!sdks.any { processInfo.commandLine.contains(it.mayaPath) }) {
            return mutableListOf()
        }

        val currentSdk = sdks.firstOrNull { processInfo.commandLine.contains(it.mayaPath) } ?: return mutableListOf()
        return mutableListOf(MayaAttachDebugger(PythonSdkType.findSdkByPath(currentSdk.mayaPyPath)!!))
    }

    override fun isAttachHostApplicable(attachHost: XAttachHost): Boolean {
        return true
    }

    private class MayaAttachDebugger(sdk: Sdk) : XAttachDebugger {
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
}

class MayaAttachGroup : XAttachProcessPresentationGroup {
    companion object {
        val INSTANCE = MayaAttachGroup()
    }

    override fun getProcessDisplayText(project: Project, info: ProcessInfo, userData: UserDataHolder): String {
        return info.commandLine
    }

    override fun getProcessIcon(p0: Project, p1: ProcessInfo, p2: UserDataHolder): Icon {
        return IconLoader.getIcon("/icons/MayaCharm_ToolWindow.png")
    }

    override fun getGroupName(): String {
        return "Maya"
    }

    override fun getOrder(): Int {
        return 0
    }
}
