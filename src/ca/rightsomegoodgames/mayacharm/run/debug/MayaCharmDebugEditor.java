package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.openapi.project.Project;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfigurationEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaCharmDebugEditor extends PyRemoteDebugConfigurationEditor {
    private MayaCharmDebugForm myForm;

    public MayaCharmDebugEditor(Project project, MayaCharmDebugConfig config) {
        super(project, config);
        myForm = new MayaCharmDebugForm(project, config);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myForm.GetPanel();
    }
}
