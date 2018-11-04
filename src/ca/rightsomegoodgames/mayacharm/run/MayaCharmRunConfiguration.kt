package ca.rightsomegoodgames.mayacharm.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.*
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import com.intellij.util.xmlb.SkipDefaultValuesSerializationFilters
import com.intellij.util.xmlb.XmlSerializer
import org.jdom.Element
import java.io.File

enum class ExecutionType {FILE, CODE}

private val SERIALIZATION_FILTERS = SkipDefaultValuesSerializationFilters()

class MayaCharmRunConfiguration(project: Project, factory: ConfigurationFactory?, name: String?)
    : RunConfigurationBase(project, factory, name) {

    public var scriptFilePath: String = ""
    public var scriptCodeText: String = ""
    public var executionType: ExecutionType = ExecutionType.FILE

    data class ConfigurationState(
        var scriptFilePath: String = "",
        var scriptCodeText: String = "",
        var executionType: ExecutionType = ExecutionType.FILE
    )

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        return RunConfigEditor(this)
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
        val state = XmlSerializer.deserialize(element, ConfigurationState::class.java)
        scriptFilePath = state.scriptFilePath
        scriptCodeText = state.scriptCodeText
        executionType = state.executionType
    }

    override fun writeExternal(element: Element) {
        val state = ConfigurationState()
        state.scriptFilePath = scriptFilePath
        state.scriptCodeText = scriptCodeText
        state.executionType = executionType

        XmlSerializer.serializeInto(state, element, SERIALIZATION_FILTERS)
        super.writeExternal(element)
    }

    override fun checkConfiguration() {
        when (executionType) {
            ExecutionType.CODE -> {
                if (scriptCodeText.isBlank())
                    throw RuntimeConfigurationException("Code field is empty")
            }
            ExecutionType.FILE -> {
                if (scriptFilePath.isBlank() || !File(scriptFilePath).isFile)
                    throw RuntimeConfigurationException("File does not exist")
            }
        }
    }

    override fun getState(executor: Executor, executionEnv: ExecutionEnvironment): RunProfileState? {
        return RunProfileState { p0, p1 -> null }
    }
}
