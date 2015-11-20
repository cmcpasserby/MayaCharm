package ca.rightsomegoodgames.mayacharm.run.configuration;

import ca.rightsomegoodgames.mayacharm.settings.MCSettingsProvider;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaCharmConfigEditor extends SettingsEditor<MayaCharmRunConfiguration> {
    private final MayaCharmRunConfiguration configuration;
    private final MCSettingsProvider settingsProvider;
    private MayaCharmConfigForm myForm;

    public MayaCharmConfigEditor(MayaCharmRunConfiguration configuration) {
        settingsProvider = MCSettingsProvider.getInstance(configuration.getProject());
        this.configuration = configuration;
        this.myForm = new MayaCharmConfigForm(this.configuration.getProject());
    }

    @Override
    protected void resetEditorFrom(MayaCharmRunConfiguration configuration) {
        myForm.setCodeSnippet(settingsProvider.getCodeSnippet());
        myForm.setFilePath(settingsProvider.getFilePath());
    }

    @Override
    protected void applyEditorTo(MayaCharmRunConfiguration configuration) throws ConfigurationException {
        settingsProvider.setFilePath(myForm.getFilePath());
        settingsProvider.setCodeSnippet(myForm.getCodeSnippet());
        resetEditorFrom(configuration);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myForm.getRootPanel();
    }
}
