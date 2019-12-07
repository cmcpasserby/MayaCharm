package run

import MayaBundle as Loc
import settings.ApplicationSettings
import settings.ui.SdkSelector
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.options.SettingsEditor
import com.intellij.openapi.ui.TextFieldWithBrowseButton
import com.intellij.ui.EditorTextField
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.*

class RunConfigEditor(config: MayaCharmRunConfiguration) : SettingsEditor<MayaCharmRunConfiguration>() {
    private val myPanel = JPanel(GridBagLayout())

    private val buttonGroup = ButtonGroup()
    private val sdkSelector = SdkSelector()

    private val fileRadioButton = JRadioButton(Loc.message("mayacharm.configedit.ExecuteFile"), true).apply { update(this) }
    private val fileField = TextFieldWithBrowseButton().apply {
        val fileTypeFilter = FileChooserDescriptor(true, false, false, false, false, false)
        addBrowseFolderListener(
                Loc.message("mayacharm.configedit.SelectFile"),
                Loc.message("mayacharm.configedit.SelectFileLong"),
                config.project,
                fileTypeFilter
        )
    }

    private val codeRadioButton = JRadioButton(Loc.message("mayacharm.configedit.ExecuteCode")).apply { update(this) }
    private val codeField = EditorTextField("", config.project, FileTypeManager.getInstance().getFileTypeByExtension(".py")).apply {
        setOneLineMode(false)
    }

    init {
        with(GridBagConstraints()) {
            insets = Insets(2, 2, 2, 2)
            gridx = 0
            gridy = 0
            fill = GridBagConstraints.HORIZONTAL

            gridwidth = 2
            myPanel.add(sdkSelector, this)

            gridy++
            myPanel.add(fileRadioButton, this)

            gridy++
            gridwidth = 1
            weightx = 0.0
            myPanel.add(JLabel(Loc.message("mayacharm.configedit.File"), JLabel.RIGHT), this)
            gridx = 1
            weightx = 1.0
            myPanel.add(fileField, this)

            gridy++
            gridwidth = 2
            gridx = 0
            weightx = 1.0
            myPanel.add(codeRadioButton, this)

            gridy++
            gridwidth = 1
            weightx = 0.0
            myPanel.add(JLabel(Loc.message("mayacharm.configedit.Code"), JLabel.RIGHT), this)
            gridx = 1
            weightx = 1.0
            myPanel.add(codeField, this)
        }

        codeField.isEnabled = codeRadioButton.isSelected
        fileField.isEnabled = fileRadioButton.isSelected
    }

    private fun update(btn: JRadioButton) {
        buttonGroup.add(btn)
        btn.addItemListener {
            codeField.isEnabled = codeRadioButton.isSelected
            fileField.isEnabled = fileRadioButton.isSelected
        }
    }

    override fun resetEditorFrom(config: MayaCharmRunConfiguration) {
        sdkSelector.items = ApplicationSettings.INSTANCE.mayaSdkMapping.keys.toList().sorted()
        sdkSelector.selectedItem = config.mayaSdkPath

        fileField.text = config.scriptFilePath
        codeField.text = config.scriptCodeText
        fileRadioButton.isSelected = config.executionType == ExecutionType.FILE
        codeRadioButton.isSelected = config.executionType == ExecutionType.CODE

        codeField.isEnabled = codeRadioButton.isSelected
        fileField.isEnabled = fileRadioButton.isSelected
    }

    override fun applyEditorTo(config: MayaCharmRunConfiguration) {
        config.mayaSdkPath = sdkSelector.selectedItem ?: ""
        config.scriptFilePath = fileField.text
        config.scriptCodeText = codeField.text
        config.executionType = if (codeRadioButton.isSelected) ExecutionType.CODE else ExecutionType.FILE
    }

    override fun createEditor(): JComponent {
        return myPanel
    }
}
