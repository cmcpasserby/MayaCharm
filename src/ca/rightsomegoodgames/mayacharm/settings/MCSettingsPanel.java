package ca.rightsomegoodgames.mayacharm.settings;

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.EditorTextField;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class MCSettingsPanel {
    private JPanel myPanel;
    private JTextField hostField;
    private JTextField portField;
    private EditorTextField mayaString;

    private final String mayaDefaultString;
    private final  MCSettingsProvider mcSettingsProvider;
    private Project project;

    public MCSettingsPanel(MCSettingsProvider provider, Project project) {
        mcSettingsProvider = provider;
        this.project = project;
        mayaDefaultString = mayaString.getText();

        portField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mayaString.setText(String.format(mayaDefaultString, portField.getText(), portField.getText()));
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mayaString.setText(String.format(mayaDefaultString, portField.getText(), portField.getText()));
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                mayaString.setText(String.format(mayaDefaultString, portField.getText(), portField.getText()));
            }
        });
        reset();
    }

    public JComponent createPanel() {
        return myPanel;
    }

    public boolean isModified(){
        return getPythonCommandPort() != mcSettingsProvider.getPort() ||
                !getPythonHost().equals(mcSettingsProvider.getHost());
    }

    public int getPythonCommandPort() {
        return StringUtil.parseInt(portField.getText(), -1);
    }

    public void setPythonCommandPort(Integer value) {
        portField.setText(value.toString());
    }

    public String getPythonHost() {
        return hostField.getText();
    }

    public void setPythonHost(String value) {
        hostField.setText(value);
    }

    public void apply() {
        mcSettingsProvider.setPort(getPythonCommandPort());
        mcSettingsProvider.setHost(getPythonHost());
        reset();
    }

    public void reset() {
        setPythonCommandPort(mcSettingsProvider.getPort());
        setPythonHost(mcSettingsProvider.getHost());
    }

    private void createUIComponents() {
        mayaString = new EditorTextField("", project, FileTypeManager.getInstance().getFileTypeByExtension(".py"));
        mayaString.setOneLineMode(false);
        mayaString.setEnabled(false);
    }
}
