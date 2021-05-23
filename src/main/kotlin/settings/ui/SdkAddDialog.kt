package settings.ui

import MayaBundle as Loc
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel

class SdkAddDialog(project: Project) : DialogWrapper(project, false) {
    private val myPanel = JPanel(GridBagLayout())
    private val sdkField = TextFieldWithBrowseButton()

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
            myPanel.add(sdkField, this)
        }
    }

    override fun createCenterPanel(): JComponent = myPanel

    val result: String?
        get() = if (isOK) sdkField.text else null
}
