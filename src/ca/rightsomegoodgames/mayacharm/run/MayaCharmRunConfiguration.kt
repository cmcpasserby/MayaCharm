package ca.rightsomegoodgames.mayacharm.run

import com.intellij.execution.Executor
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.execution.configurations.RunConfigurationBase
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.project.Project
import org.jdom.Element

class MayaCharmRunConfiguration(project: Project, factory: ConfigurationFactory?, name: String?)
    : RunConfigurationBase(project, factory, name) {

    data class ConfigurationState(var scriptFilePath: String, var scriptCodeText: String) // TODO: Execution type

    override fun getConfigurationEditor(): SettingsEditor<out RunConfiguration> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun readExternal(element: Element) {
        super.readExternal(element)
    }

    override fun writeExternal(element: Element) {
        super.writeExternal(element)
    }

    override fun checkConfiguration() {
        super.checkConfiguration()
    }

    override fun getState(executor: Executor, executionEnv: ExecutionEnvironment): RunProfileState? {
        return null
    }
}
