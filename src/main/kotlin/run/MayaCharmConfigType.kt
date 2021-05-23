package run

import MayaBundle as Loc
import com.intellij.execution.configurations.ConfigurationFactory
import com.intellij.execution.configurations.ConfigurationType
import com.intellij.execution.configurations.RunConfiguration
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

class MayaCharmConfigType : ConfigurationType {
    override fun getDisplayName(): String {
        return "MayaCharm"
    }

    override fun getConfigurationTypeDescription(): String = Loc.message("mayacharm.runconfig.ConfigType")

    override fun getIcon(): Icon = IconLoader.getIcon("/icons/MayaCharm_ToolWindow.png", this::class.java)

    override fun getId(): String = "MAYACHARM_RUN_CONFIGURATION"

    override fun getConfigurationFactories(): Array<ConfigurationFactory> = arrayOf(MayaCharmConfigFactory(this))
}

class MayaCharmConfigFactory(type: MayaCharmConfigType) : ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration =
        MayaCharmRunConfiguration(project, this, "MayaCharm")

    override fun getName(): String = Loc.message("mayacharm.runconfig.ConfigFactory")

    override fun getId(): String = "MAYACHARM_RUN_CONFIGURATION"
}
