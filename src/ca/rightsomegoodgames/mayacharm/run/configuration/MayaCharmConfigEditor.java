package ca.rightsomegoodgames.mayacharm.run.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaCharmConfigEditor extends SettingsEditor<MayaCharmRunConfiguration> {
    private MayaCharmConfigForm myForm;

    public MayaCharmConfigEditor(MayaCharmRunConfiguration configuration) {
        this.myForm = new MayaCharmConfigForm(configuration.getProject());
    }

    @Override
    protected void resetEditorFrom(MayaCharmRunConfiguration configuration) {
        myForm.setFilePath(configuration.getScriptFilePath());
        myForm.setCodeText(configuration.getScriptCodeText());
    }

    @Override
    protected void applyEditorTo(MayaCharmRunConfiguration configuration) throws ConfigurationException {
        configuration.setScriptCodeText(myForm.getCodeText());
        configuration.setScriptFilePath(myForm.getFilePath());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myForm.getRootPanel();
    }
}
