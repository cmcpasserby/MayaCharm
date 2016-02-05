package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.openapi.project.Project;
import com.intellij.util.PathMappingSettings;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfigurationParams;

import javax.swing.*;

public class MayaCharmDebugForm implements PyRemoteDebugConfigurationParams {
    private JPanel rootPanel;

    public MayaCharmDebugForm(Project project, MayaCharmDebugConfig config) {
    }

    public JComponent getPanel() {
        return rootPanel;
    }

    @Override
    public int getPort() {
        return 60059;
    }

    @Override
    public void setPort(int i) {

    }

    @Override
    public String getHost() {
        return "localhost";
    }

    @Override
    public void setHost(String s) {

    }

    @Override
    public boolean isRedirectOutput() {
        return false;
    }

    @Override
    public void setRedirectOutput(boolean b) {

    }

    @Override
    public boolean isSuspendAfterConnect() {
        return false;
    }

    @Override
    public void setSuspendAfterConnect(boolean b) {

    }

    @Override
    public void setMappingSettings(PathMappingSettings pathMappingSettings) {

    }

    @Override
    public PathMappingSettings getMappingSettings() {
        return null;
    }
}
