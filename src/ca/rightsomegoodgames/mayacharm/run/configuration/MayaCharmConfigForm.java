package ca.rightsomegoodgames.mayacharm.run.configuration;

import com.intellij.openapi.fileTypes.FileTypeManager;
import com.intellij.openapi.project.Project;
import com.intellij.ui.EditorTextField;

import javax.swing.*;

public class MayaCharmConfigForm {
    private Project project;

    private JPanel rootPanel;
    private JRadioButton fileRadioButton;
    private JRadioButton codeRadioButton;
    private JPanel filePanel;
    private JPanel codePanel;
    private EditorTextField codeField;

    public MayaCharmConfigForm(Project project) {
        this.project = project;
    }

    public JComponent getRootPanel() {
        return rootPanel;
    }

    public String getInitCode() {
        return codeField.getText();
    }

    private void createUIComponents() {
        codeField = new EditorTextField("", project, FileTypeManager.getInstance().getFileTypeByExtension(".py"));
        codeField.setOneLineMode(false);
    }
}
