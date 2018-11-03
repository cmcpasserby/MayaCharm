package ca.rightsomegoodgames.mayacharm.settings;

import ca.rightsomegoodgames.mayacharm.resources.PythonStrings;
import com.intellij.openapi.util.text.StringUtil;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SettingsPanel {
    private JPanel myPanel;
    private JTextField hostField;
    private JTextField portField;
    private JTextArea mayaString;

    private final  SettingsProvider mcSettingsProvider;

    public SettingsPanel(SettingsProvider provider) {
        mcSettingsProvider = provider;
        portField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                mayaString.setText(formatCmdText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                mayaString.setText(formatCmdText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                mayaString.setText(formatCmdText());
            }
        });
        reset();
    }

    private String formatCmdText() {
        return String.format(PythonStrings.INSTANCE.getCmdportSetupScript(), portField.getText());
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
    }

    public void reset() {
        setPythonCommandPort(mcSettingsProvider.getPort());
        setPythonHost(mcSettingsProvider.getHost());
    }
}
