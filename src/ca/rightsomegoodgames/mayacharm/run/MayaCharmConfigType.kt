package ca.rightsomegoodgames.mayacharm.run

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

    override fun getConfigurationTypeDescription(): String {
        return "MayaCharm Run Configuration Type"
    }

    override fun getIcon(): Icon {
        return IconLoader.getIcon("/icons/MayaCharm_ToolWindow.png")
    }

    override fun getId(): String {
        return "MAYACHARM_RUN_CONFIGURATION"
    }

    override fun getConfigurationFactories(): Array<ConfigurationFactory> {
        return arrayOf(MayaCharmConfigFactory(this))
    }
}

class MayaCharmConfigFactory(type: MayaCharmConfigType) : ConfigurationFactory(type) {
    override fun createTemplateConfiguration(project: Project): RunConfiguration {
        return MayaCharmRunConfiguration(project, this, "MayaCharm")
    }

    override fun getName(): String {
        return "MayaCharm Configuration Factory"
    }
}
