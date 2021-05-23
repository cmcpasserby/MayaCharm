package run

import MayaBundle as Loc
import settings.ProjectSettings

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element
import java.io.File

enum class ExecutionType { FILE, CODE }

class MayaCharmRunConfiguration(project: Project, factory: ConfigurationFactory?, name: String?) :
    RunConfigurationBase<MayaCharmRunConfiguration.ConfigurationState>(project, factory, name) {
    var mayaSdkPath: String = ProjectSettings.getInstance(project).selectedSdkName ?: ""
    var scriptFilePath: String = ""
    var scriptCodeText: String = ""
    var executionType: ExecutionType = ExecutionType.FILE

    data class ConfigurationState(
        var mayaSdkPath: String = "",
        var scriptFilePath: String = "",
        var scriptCodeText: String = "",
        var executionType: ExecutionType = ExecutionType.FILE
    )

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> = RunConfigEditor(this)

    override fun readExternal(element: Element) {
        super.readExternal(element)
        XmlSerializer.deserialize(element, ConfigurationState::class.java).also {
            mayaSdkPath = it.mayaSdkPath
            scriptFilePath = it.scriptFilePath
            scriptCodeText = it.scriptCodeText
            executionType = it.executionType
        }
    }

    override fun writeExternal(element: Element) {
        ConfigurationState(mayaSdkPath, scriptFilePath, scriptCodeText, executionType).apply {
            XmlSerializer.serializeInto(this, element)
            super.writeExternal(element)
        }
    }

    override fun checkConfiguration() {
        if (mayaSdkPath.isBlank())
            throw RuntimeConfigurationException(Loc.message("mayacharm.runconfig.SdkNotSelected"))

        when (executionType) {
            ExecutionType.CODE -> {
                if (scriptCodeText.isBlank())
                    throw RuntimeConfigurationException(Loc.message("mayacharm.runconfig.CodeFieldIsEmpty"))
            }
            ExecutionType.FILE -> {
                if (scriptFilePath.isBlank() || !File(scriptFilePath).isFile)
                    throw RuntimeConfigurationException(Loc.message("mayacharm.runconfig.FilesNotExist"))
            }
        }
    }

    override fun getState(executor: Executor, executionEnv: ExecutionEnvironment): RunProfileState =
        RunProfileState { _, _ -> null }
}
