package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.project.Project;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfiguration;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfigurationEditor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaCharmDebugEditor extends PyRemoteDebugConfigurationEditor {
    private MayaCharmDebugForm myForm;

    @Override
    protected void resetEditorFrom(PyRemoteDebugConfiguration pyRemoteDebugConfiguration) {
        final MayaCharmDebugConfig config = (MayaCharmDebugConfig) pyRemoteDebugConfiguration;
        myForm.setFilePath(config.getScriptFilePath());
        myForm.setCodeText(config.getScriptCodeText());
        myForm.setUseCode(config.isUseCode());
        myForm.setHost(config.getHost());
        myForm.setPort(config.getPort());
        myForm.setRedirectOutput(config.isRedirectOutput());
        myForm.setSuspendAfterConnect(config.isSuspendAfterConnect());
//        super.resetEditorFrom(pyRemoteDebugConfiguration);
    }

    @Override
    protected void applyEditorTo(PyRemoteDebugConfiguration pyRemoteDebugConfiguration) throws ConfigurationException {
        final MayaCharmDebugConfig config = (MayaCharmDebugConfig) pyRemoteDebugConfiguration;
        config.setScriptFilePath(myForm.getFilePath());
        config.setScriptCodeText(myForm.getCodeText());
        config.setUseCode(myForm.getUseCode());
        config.setHost(myForm.getHost());
        config.setPort(myForm.getPort());
        config.setRedirectOutput(myForm.isRedirectOutput());
        config.setSuspendAfterConnect(myForm.isSuspendAfterConnect());
//        super.applyEditorTo(pyRemoteDebugConfiguration);
    }

    public MayaCharmDebugEditor(Project project, MayaCharmDebugConfig config) {
        super(project, config);
        myForm = new MayaCharmDebugForm(project, config);
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myForm.getPanel();
    }
}
