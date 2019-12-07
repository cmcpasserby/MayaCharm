package run

import mayacomms.MayaCommandInterface
import settings.ApplicationSettings

import com.intellij.execution.ExecutionException
import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.configurations.RuntimeConfigurationException
import com.intellij.execution.executors.DefaultRunExecutor
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.GenericProgramRunner
import com.intellij.execution.ui.RunContentDescriptor
import com.intellij.openapi.fileEditor.FileDocumentManager

class MayaCharmRunner : GenericProgramRunner<RunnerSettings>() {
    override fun getRunnerId(): String {
        return "MayaCharmRunner"
    }

    override fun canRun(executorId: String, runProfile: RunProfile): Boolean {
        val runConfig = runProfile as? MayaCharmRunConfiguration ?: return false

        try {
            runConfig.checkConfiguration()
        } catch (e: RuntimeConfigurationException) {
            return false
        }

        return DefaultRunExecutor.EXECUTOR_ID == executorId
    }

    @Throws(ExecutionException::class)
    override fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        val appSettings = ApplicationSettings.INSTANCE
        FileDocumentManager.getInstance().saveAllDocuments()

        val config = environment.runProfile as MayaCharmRunConfiguration

        val maya = MayaCommandInterface(appSettings.mayaSdkMapping[config.mayaSdkPath]!!.port)

        when (config.executionType) {
            ExecutionType.FILE -> maya.sendFileToMaya(config.scriptFilePath)
            ExecutionType.CODE -> maya.sendCodeToMaya(config.scriptCodeText)
        }

        return null
    }
}
