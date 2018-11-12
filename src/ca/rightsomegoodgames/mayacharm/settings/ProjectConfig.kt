package ca.rightsomegoodgames.mayacharm.settings

import com.intellij.openapi.Disposable
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import javax.swing.JComponent

const val CONSOLE_SETTINGS_HELP_REFERENCE = "reference.settings.ssh.terminal"

class ProjectConfig(project: Project) : Configurable, SearchableConfigurable, Configurable.NoScroll, Disposable {
    private val settings = ProjectSettings.getInstance(project)
    private var myPanel: SettingsPanel? = null

    override fun getDisplayName(): String {
        return "MayaCharm"
    }

    override fun getHelpTopic(): String {
        return CONSOLE_SETTINGS_HELP_REFERENCE
    }

    override fun getId(): String {
        return "mayacharm"
    }

    override fun enableSearch(option: String?): Runnable? {
        return null
    }

    override fun createComponent(): JComponent? {
        myPanel = SettingsPanel(settings)
        return myPanel?.createPanel()
    }

    override fun isModified(): Boolean {
        return myPanel!!.isModified
    }

    override fun apply() {
        myPanel?.apply()
    }

    override fun reset() {
        myPanel?.reset()
    }

    override fun disposeUIResources() {
        Disposer.dispose(this)
    }

    override fun dispose() {
        myPanel = null
    }
}
