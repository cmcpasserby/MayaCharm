package settings.ui

import MayaBundle as Loc
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.ui.DialogWrapper
import com.jetbrains.python.sdk.PyDetectedSdk
import com.jetbrains.python.sdk.add.PySdkPathChoosingComboBox
import com.jetbrains.python.sdk.setup
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class SdkAddDialog(project: Project, private val existingSdks: List<Sdk>) : DialogWrapper(project, false) {
    private val myPanel = JPanel(GridBagLayout())
    private val sdkChooser = PySdkPathChoosingComboBox(existingSdks, null)

    init {
        title = Loc.message("mayacharm.sdkadd.AddMayaSdk")
        init()

        with(GridBagConstraints()) {
            insets = Insets(2, 2, 2, 2)
            gridx = 0
            gridy = 0
            fill = GridBagConstraints.HORIZONTAL
            gridwidth = 1

            weightx = 0.0
            myPanel.add(JLabel(Loc.message("mayacharm.sdkadd.Interpreter"), JLabel.RIGHT), this)

            gridx = 1
            weightx = 1.0
            myPanel.add(sdkChooser, this)
        }
    }

    override fun createCenterPanel(): JComponent {
        return myPanel
    }

    public fun getOrCreateSdk(): Sdk? {
        return when(val sdk = sdkChooser.selectedSdk) {
            is PyDetectedSdk -> sdk.setup(existingSdks)
            else -> sdk
        }
    }
}
