package ca.rightsomegoodgames.mayacharm.run

import com.intellij.openapi.options.SettingsEditor
import javax.swing.JComponent

class RunConfigEditor(config: MayaCharmRunConfiguration) : SettingsEditor<MayaCharmRunConfiguration>() {
    private val myForm: RunConfigForm = RunConfigForm(config.project)

    override fun resetEditorFrom(config: MayaCharmRunConfiguration) {
        myForm.filePath = config.scriptFilePath
        myForm.codeText = config.scriptCodeText
        myForm.executionType = config.executionType
    }

    override fun applyEditorTo(config: MayaCharmRunConfiguration) {
        config.scriptCodeText = myForm.codeText
        config.scriptFilePath = myForm.filePath
        config.executionType = myForm.executionType
    }

    override fun createEditor(): JComponent {
        return myForm.rootPanel
    }
}
