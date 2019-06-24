package ca.rightsomegoodgames.mayacharm.settings.ui

import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.ui.DialogWrapper
import com.jetbrains.python.sdk.add.PySdkPathChoosingComboBox
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class SdkAddDialog(project: Project, existingSdks: List<Sdk>) : DialogWrapper(project, false) {
    private val myPanel = JPanel(GridBagLayout())
    private val sdkChooser = PySdkPathChoosingComboBox(existingSdks, null)

    init {
        title = "Add Maya Sdk"
        init()

        with(GridBagConstraints()) {
            insets = Insets(2, 2, 2, 2)
            gridx = 0
            gridy = 0
            fill = GridBagConstraints.HORIZONTAL
            gridwidth = 1

            weightx = 0.0
            myPanel.add(JLabel("Interpreter: ", JLabel.RIGHT), this)

            gridx = 1
            weightx = 1.0
            myPanel.add(sdkChooser, this)
        }
    }

    override fun createCenterPanel(): JComponent? {
        return myPanel
    }

    public val selectedSdk: Sdk?
        get() = sdkChooser.selectedSdk
}
