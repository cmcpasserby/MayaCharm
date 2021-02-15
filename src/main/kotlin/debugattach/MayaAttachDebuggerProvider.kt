package debugattach

import settings.ApplicationSettings
import utils.pathForPid
import com.intellij.execution.process.ProcessInfo
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.util.IconLoader
import com.intellij.openapi.util.UserDataHolder
import com.intellij.xdebugger.attach.*
import com.jetbrains.python.sdk.PythonSdkType
import com.jetbrains.python.sdk.PythonSdkUtil
import javax.swing.Icon


class MayaAttachDebuggerProvider : XAttachDebuggerProvider {
    override fun getPresentationGroup(): XAttachPresentationGroup<ProcessInfo> {
        return MayaAttachGroup.INSTANCE
    }

    override fun getAvailableDebuggers(
        project: Project,
        attachHost: XAttachHost,
        processInfo: ProcessInfo,
        userData: UserDataHolder
    ): MutableList<XAttachDebugger> {
        if (!processInfo.executableName.toLowerCase().contains("maya")) return mutableListOf()

        val exePath = processInfo.executableCannonicalPath.let {
            if (it.isPresent) it.get() else pathForPid(processInfo.pid) ?: return mutableListOf()
        }.toLowerCase()

        val currentSdk = ApplicationSettings.INSTANCE.mayaSdkMapping.values.firstOrNull {
            exePath.contains(it.mayaPath.toLowerCase())
        } ?: return mutableListOf()

        PythonSdkUtil.findSdkByPath(currentSdk.mayaPyPath)?.let {
            return mutableListOf(MayaAttachDebugger(it, currentSdk))
        }

        return mutableListOf()
    }

    override fun isAttachHostApplicable(attachHost: XAttachHost): Boolean = attachHost is LocalAttachHost
}

private class MayaAttachDebugger(sdk: Sdk, private val mayaSdk: ApplicationSettings.SdkInfo) : XAttachDebugger {
    private val mySdkHome: String? = sdk.homePath
    private val myName: String = "${PythonSdkType.getInstance().getVersionString(sdk)} ($mySdkHome)"

    override fun getDebuggerDisplayName(): String {
        return myName
    }

    override fun attachDebugSession(project: Project, attachHost: XAttachHost, processInfo: ProcessInfo) {
        val runner = MayaAttachToProcessDebugRunner(project, processInfo.pid, mySdkHome)
        runner.launch()
    }
}

private class MayaAttachGroup : XAttachProcessPresentationGroup {
    companion object {
        val INSTANCE = MayaAttachGroup()
    }

    override fun getItemDisplayText(project: Project, processInfo: ProcessInfo, userData: UserDataHolder): String {
        return processInfo.executableDisplayName
    }

    override fun getProcessDisplayText(project: Project, info: ProcessInfo, userData: UserDataHolder): String {
        return getItemDisplayText(project, info, userData)
    }

    override fun getItemIcon(project: Project, processInfo: ProcessInfo, userData: UserDataHolder): Icon {
        return IconLoader.getIcon("/icons/MayaCharm_ToolWindow.png", this::class.java)
    }

    override fun getProcessIcon(project: Project, info: ProcessInfo, userData: UserDataHolder): Icon {
        return getItemIcon(project, info, userData)
    }

    override fun getGroupName(): String {
        return "Maya"
    }

    override fun getOrder(): Int {
        return -100
    }
}
