package ca.rightsomegoodgames.mayacharm.run

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.settings.ApplicationSettings
import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.GenericProgramRunner
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.fileEditor.FileDocumentManager

class MayaCharmRunner : GenericProgramRunner<RunnerSettings>() {
    override fun getRunnerId(): String {
        return "MayaCharmRunner"
    }

    override fun canRun(s: String, runProfile: RunProfile): Boolean {
        return runProfile is MayaCharmRunConfiguration && s == "Run"
    }

    @Throws(ExecutionException::class)
    override fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        val appSettings = ApplicationSettings.getInstance()
        FileDocumentManager.getInstance().saveAllDocuments()

        val config = environment.runProfile as MayaCharmRunConfiguration

        // TODO fixme here, enforce that the sdkMapping cant return null
        val maya = MayaCommandInterface(appSettings.mayaSdkMapping[config.mayaSdkPath]!!.port)

        when (config.executionType) {
            ExecutionType.FILE -> maya.sendFileToMaya(config.scriptFilePath)
            ExecutionType.CODE -> maya.sendCodeToMaya(config.scriptCodeText)
        }

        return null
    }
}
