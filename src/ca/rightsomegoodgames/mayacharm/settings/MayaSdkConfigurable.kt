package ca.rightsomegoodgames.mayacharm.settings

import ca.rightsomegoodgames.mayacharm.settings.ui.SdkSelector
import ca.rightsomegoodgames.mayacharm.settings.ui.SdkTablePanel
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

    private val settings = ApplicationSettings.INSTANCE
    private val projectSettings = ProjectSettings.getInstance(project)

    private val myPanel = JPanel(GridBagLayout())
    private val mySdkSelector = SdkSelector()
    private val mySdkPanel = SdkTablePanel(project)

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
        val entries = settings.mayaSdkMapping.values.toSet() != mySdkPanel.data.toSet()
        val selected = mySdkSelector.selectedItem != projectSettings.selectedSdkName
        return entries || selected
    }

    override fun reset() {
        mySdkPanel.data.clear()
        mySdkPanel.data.addAll(settings.mayaSdkMapping.values.sortedBy { it.mayaPyPath })

        mySdkSelector.items = settings.mayaSdkMapping.keys.sorted()
        mySdkSelector.selectedItem = projectSettings.selectedSdkName
    }

    override fun apply() {
        settings.mayaSdkMapping = mySdkPanel.data.map { it.mayaPyPath to it }.toMap().toMutableMap()
        projectSettings.selectedSdkName = mySdkSelector.selectedItem
    }
}
