package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.EditorTextField;
import com.intellij.util.PathMappingSettings;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfigurationParams;

import javax.swing.*;

public class MayaCharmDebugForm implements PyRemoteDebugConfigurationParams {
    private final Project project;
    private final MayaCharmDebugConfig config;

    private JPanel rootPanel;
    private JRadioButton fileRadioButton;
    private JRadioButton codeRadioButton;
    private TextFieldWithBrowseButton fileField;
    private EditorTextField codeField;
    private JTextField hostnameField;
    private JTextField portField;
    private JCheckBox redirectOutputField;
    private JCheckBox suspendField;

    public MayaCharmDebugForm(Project project, MayaCharmDebugConfig config) {
        this.project = project;
        this.config = config;
        UpdateControls();
        fileRadioButton.addItemListener(e -> UpdateControls());
        codeRadioButton.addItemListener(e -> UpdateControls());
    }

    private void UpdateControls() {
        codeField.setEnabled(codeRadioButton.isSelected());
        fileField.setEnabled(fileRadioButton.isSelected());
    }

    public JComponent getPanel() {
        return rootPanel;
    }

    public String getCodeText() {
        return codeField.getText();
    }

    public void setCodeText(String value) {
        codeField.setText(value);
    }

    public String getFilePath() {
        return fileField.getText();
    }

    public void setFilePath(String value) {
        fileField.setText(value);
    }

    public boolean getUseCode() {
        return codeRadioButton.isSelected();
    }

    public void setUseCode(boolean value) {
        codeRadioButton.setSelected(value);
        fileRadioButton.setSelected(!value);
    }

    @Override
    public int getPort() {
        return StringUtil.parseInt(portField.getText(), -1);
    }

    @Override
    public void setPort(int i) {
        portField.setText(Integer.toString(i));
    }

    @Override
    public String getHost() {
        return hostnameField.getText();
    }

    @Override
    public void setHost(String s) {
        hostnameField.setText(s);
    }

    @Override
    public boolean isRedirectOutput() {
        return redirectOutputField.isSelected();
    }

    @Override
    public void setRedirectOutput(boolean b) {
        redirectOutputField.setSelected(b);
    }

    @Override
    public boolean isSuspendAfterConnect() {
        return suspendField.isSelected();
    }

    @Override
    public void setSuspendAfterConnect(boolean b) {
        suspendField.setSelected(b);
    }

    @Override
    public void setMappingSettings(PathMappingSettings pathMappingSettings) {

    }

    @Override
    public PathMappingSettings getMappingSettings() {
        return null;
    }

    private void createUIComponents() {
        FileChooserDescriptor fileTypeFilter = new FileChooserDescriptor(true, false, false, false, false, false);
        fileField = new TextFieldWithBrowseButton();
        fileField.addBrowseFolderListener("Select File", "Select python file to execute in maya.", project, fileTypeFilter);

        codeField = new EditorTextField("", project, FileTypeManager.getInstance().getFileTypeByExtension(".py"));
        codeField.setOneLineMode(false);
    }
}
