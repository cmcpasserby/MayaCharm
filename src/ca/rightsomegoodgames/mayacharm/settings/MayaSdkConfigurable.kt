package ca.rightsomegoodgames.mayacharm.settings

import ca.rightsomegoodgames.mayacharm.ui.SdkSelector
import ca.rightsomegoodgames.mayacharm.ui.SdkTablePanel
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import java.awt.*
import javax.swing.JComponent
import javax.swing.JPanel

class MayaSdkConfigurable(project: Project) : SearchableConfigurable, Configurable.NoScroll {
    companion object {
        public const val ID = "ca.rightsomegoodgames.mayacharm.settings.MayaSdkConfigurable"
    }

    private val settings = ApplicationSettings.getInstance()
    private val projectSettings = ProjectSettings.getInstance(project)

    private val myPanel = JPanel(GridBagLayout())
    private val mySdkSelector = SdkSelector()
    private val mySdkPanel = SdkTablePanel()

    init {
        with(GridBagConstraints()) {
            insets = Insets(2, 2, 2, 2)
            weightx = 1.0
            gridx = 0
            gridy = 0

            fill = GridBagConstraints.HORIZONTAL
            myPanel.add(mySdkSelector, this)

            insets = Insets(2, 2, 0, 2)
            gridy = 1
            weighty = 1.0
            gridheight = GridBagConstraints.RELATIVE
            fill = GridBagConstraints.BOTH
            myPanel.add(mySdkPanel, this)
        }
    }

    override fun getId(): String {
        return ID
    }

    override fun getDisplayName(): String {
        return "MayaCharm"
    }

    override fun getHelpTopic(): String? {
        return null
    }

    override fun createComponent(): JComponent {
        return myPanel
    }

    override fun isModified(): Boolean {
        val entries = settings.mayaSdkMapping.toMap() != mySdkPanel.data.map { it.first to it.second }.toMap()
        val selected = mySdkSelector.selectedItem != projectSettings.selectedSdk
        return entries || selected
    }

    override fun reset() {
        mySdkPanel.data.clear()
        mySdkPanel.data.addAll(settings.mayaSdkMapping.entries.map { it.key to it.value })

        mySdkSelector.items = settings.mayaSdkMapping.keys.toList()
        mySdkSelector.selectedItem = projectSettings.selectedSdk
    }

    override fun apply() {
        settings.mayaSdkMapping = mySdkPanel.data.toMap().toMutableMap()
        projectSettings.selectedSdk = mySdkSelector.selectedItem
    }
}
