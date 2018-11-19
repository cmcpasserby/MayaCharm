package ca.rightsomegoodgames.mayacharm.settings.ui

import ca.rightsomegoodgames.mayacharm.resources.PythonStrings
import ca.rightsomegoodgames.mayacharm.settings.ApplicationSettings
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.ui.EditorTextField
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

class SdkEditDialog(project: Project, private val sdkInfo: ApplicationSettings.SdkInfo) : DialogWrapper(project, false) {
    private var okPressed = false

    private val myPanel = JPanel(GridBagLayout())
    private val nameField = JTextField().also {
        it.text = sdkInfo.mayaPyPath
        it.isEnabled = false
    }

    private val portField = JTextField().also {
        it.text = sdkInfo.port.toString()
        it.document.addDocumentListener(object : DocumentListener {
            override fun changedUpdate(e: DocumentEvent?) {
                updateSetupText()
                updateOkButton()
            }

            override fun insertUpdate(e: DocumentEvent?) {
                updateSetupText()
                updateOkButton()
            }

            override fun removeUpdate(e: DocumentEvent?) {
                updateSetupText()
                updateOkButton()
            }
        })
    }

    private val setupText = EditorTextField(String.format(PythonStrings.INSTANCE.cmdportSetupScript, ""),
            project, FileTypeManager.getInstance().getFileTypeByExtension(".py")).also {
        it.setOneLineMode(false)
        it.isEnabled = false
    }

    init {
        title = "Edit Port Number"
        updateSetupText()
        updateOkButton()
        init()

        with(GridBagConstraints()) {
            insets = Insets(2, 2, 2, 2)
            gridx = 0
            gridy = 0
            fill = GridBagConstraints.HORIZONTAL
            gridwidth = 1

            weightx = 0.0
            myPanel.add(JLabel("SDK Name: ", JLabel.RIGHT), this)

            gridx = 1
            weightx = 1.0
            myPanel.add(nameField, this)

            gridy++
            gridx = 0
            weightx = 0.0
            myPanel.add(JLabel("Port Number: ", JLabel.RIGHT), this)

            gridx = 1
            weightx = 1.0
            myPanel.add(portField, this)

            gridy++
            gridx = 0
            weightx = 1.0
            gridwidth = 2
            myPanel.add(setupText, this)
        }
    }

    override fun createCenterPanel(): JComponent? {
        return myPanel
    }

    override fun doOKAction() {
        okPressed = true
        super.doOKAction()
    }

    public val result: ApplicationSettings.SdkInfo
        get() = if (okPressed) ApplicationSettings.SdkInfo(sdkInfo.mayaPyPath, portField.text.toInt()) else sdkInfo

    private fun updateSetupText() {
        setupText.text = String.format(PythonStrings.INSTANCE.cmdportSetupScript, portField.text)
    }

    private fun isModified(): Boolean {
        val port = portField.text.toIntOrNull() ?: return false
        return sdkInfo != ApplicationSettings.SdkInfo(sdkInfo.mayaPyPath, port)
    }

    private fun updateOkButton() {
        isOKActionEnabled = isModified()
    }
}
