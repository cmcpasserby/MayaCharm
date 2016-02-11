package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfigurationFactory;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaCharmDebugConfigFactory extends PyRemoteDebugConfigurationFactory {
    private static final String FACTORY_NAME = "MayaCharm Debug Config Factory";

    public MayaCharmDebugConfigFactory(@NotNull ConfigurationType configurationType) {
        super(configurationType);
    }

    @Override
    public Icon getIcon(@NotNull RunConfiguration configuration) {
        return IconLoader.getIcon("/icons/MayaCharm_ToolWindow.png");
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(Project project) {
        return new MayaCharmDebugConfig(project, this, "MayaCharmDebug");
    }

    @Override
    public String getName() {
        return FACTORY_NAME;
    }
}
