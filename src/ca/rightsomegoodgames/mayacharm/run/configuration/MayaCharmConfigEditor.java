package ca.rightsomegoodgames.mayacharm.run.configuration;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.ComponentWithBrowseButton;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaCharmConfigEditor extends SettingsEditor<MayaCharmRunConfiguration> {
    private final MayaCharmRunConfiguration configuration;
    private LabeledComponent<ComponentWithBrowseButton> myMainClass;

    private JPanel myPanel;

    public MayaCharmConfigEditor(MayaCharmRunConfiguration configuration) {
        this.configuration = configuration;
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
        return myPanel;
    }

    private void createUIComponents() {
        myMainClass = new LabeledComponent<>();
        myMainClass.setComponent(new TextFieldWithBrowseButton());
    }
}
