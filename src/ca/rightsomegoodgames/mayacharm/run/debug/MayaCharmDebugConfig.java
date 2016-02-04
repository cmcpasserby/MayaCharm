package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.openapi.project.Project;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfiguration;

public class MayaCharmDebugConfig extends PyRemoteDebugConfiguration {

    public MayaCharmDebugConfig(Project project, MayaCharmDebugConfigFactory configurationFactory, String s) {
        super(project, configurationFactory, s);
    }
}
