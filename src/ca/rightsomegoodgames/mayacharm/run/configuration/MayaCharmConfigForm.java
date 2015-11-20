package ca.rightsomegoodgames.mayacharm.run.configuration;

import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.EditorTextField;

import javax.swing.*;

public class MayaCharmConfigForm {
    private Project project;

    private JPanel rootPanel;
    private JRadioButton fileRadioButton;
    private JRadioButton codeRadioButton;
    private EditorTextField codeField;
    private TextFieldWithBrowseButton fileField;

    public MayaCharmConfigForm(Project project) {
        this.project = project;
    }

    public JComponent getRootPanel() {
        return rootPanel;
    }

    private void createUIComponents() {
        FileChooserDescriptor fileTypeFilter = new FileChooserDescriptor(true, false, false, false, false, false);
        fileField = new TextFieldWithBrowseButton();
        fileField.addBrowseFolderListener("Select File", "Select python file to execute in maya.", project, fileTypeFilter);

        codeField = new EditorTextField("", project, FileTypeManager.getInstance().getFileTypeByExtension(".py"));
        codeField.setOneLineMode(false);
    }

    public String getCodeSnippet() {
        return codeField.getText();
    }

    public void setCodeSnippet(String snippet) {
        codeField.setText(snippet);
    }

    public String getFilePath() {
        return fileField.getText();
    }

    public void setFilePath(String path) {
        fileField.setText(path);
    }
}
