package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.openapi.project.Project;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfiguration;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfigurationEditor;

public class MayaCharmDebugEditor extends PyRemoteDebugConfigurationEditor {
    public MayaCharmDebugEditor(Project project, PyRemoteDebugConfiguration pyRemoteDebugConfiguration) {
        super(project, pyRemoteDebugConfiguration);
    }
}
