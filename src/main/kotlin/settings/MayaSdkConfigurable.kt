package settings

import settings.ui.SdkSelector
import settings.ui.SdkTablePanel

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.project.Project
import java.awt.*
import javax.swing.JComponent
import javax.swing.JPanel
import javax.swing.event.AncestorEvent
import javax.swing.event.AncestorListener

class MayaSdkConfigurable(project: Project) : SearchableConfigurable, Configurable.NoScroll {
    companion object {
        const val ID = "settings.MayaSdkConfigurable"
    }

    private val settings = ApplicationSettings.INSTANCE
    private val projectSettings = ProjectSettings.getInstance(project)

    private val myPanel = JPanel(GridBagLayout()).also {
        it.addAncestorListener(object : AncestorListener {
            override fun ancestorAdded(event: AncestorEvent?) {
                ApplicationSettings.INSTANCE.refreshPythonSdks()
                reset()
            }

            override fun ancestorMoved(event: AncestorEvent?) {}

            override fun ancestorRemoved(event: AncestorEvent?) {}
        })
    }

    private val mySdkSelector = SdkSelector()
    private val mySdkPanel = SdkTablePanel(project).also {
        it.changed += {
            ApplicationSettings.INSTANCE.refreshPythonSdks()
            mySdkSelector.items = settings.mayaSdkMapping.keys.sorted()
            mySdkSelector.selectedItem = projectSettings.selectedSdkName
        }
    }

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

    override fun getId(): String = ID

    override fun getDisplayName(): String = "MayaCharm"

    override fun getHelpTopic(): String? = null // TODO

    override fun createComponent(): JComponent = myPanel

    override fun isModified(): Boolean {
        val entries = settings.mayaSdkMapping.values.toSet() != mySdkPanel.data.toSet()
        val selected = mySdkSelector.selectedItem != projectSettings.selectedSdkName
        return entries || selected
    }

    override fun reset() {
        mySdkPanel.data.clear()
        mySdkPanel.data.addAll(settings.mayaSdkMapping.values.sortedBy { it.mayaPath })

        mySdkSelector.items = settings.mayaSdkMapping.keys.sorted()
        mySdkSelector.selectedItem = projectSettings.selectedSdkName
    }

    override fun apply() {
        settings.mayaSdkMapping = mySdkPanel.data.associateBy { it.mayaPath }.toMutableMap()
        projectSettings.selectedSdkName = mySdkSelector.selectedItem
    }
}
