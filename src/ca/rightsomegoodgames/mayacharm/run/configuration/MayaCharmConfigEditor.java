package ca.rightsomegoodgames.mayacharm.run.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaCharmConfigEditor extends SettingsEditor<MayaCharmRunConfiguration> {
    private final MayaCharmRunConfiguration configuration;
    private MayaCharmConfigForm myForm;

    public MayaCharmConfigEditor(MayaCharmRunConfiguration configuration) {
        this.configuration = configuration;
        this.myForm = new MayaCharmConfigForm(this.configuration.getProject());
    }

    @Override
    protected void resetEditorFrom(MayaCharmRunConfiguration configuration) {

    }

    @Override
    protected void applyEditorTo(MayaCharmRunConfiguration configuration) throws ConfigurationException {

    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myForm.getRootPanel();
    }
}
