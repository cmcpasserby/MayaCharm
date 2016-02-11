package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.openapi.util.IconLoader;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfigurationType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaCharmDebugConfigType extends PyRemoteDebugConfigurationType {
    @Override
    public String getDisplayName() {
        return "MayaCharm Debugger";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "MayaCharm Remote Debugger Configuration Type";
    }

    @Override
    public Icon getIcon() {
        return IconLoader.getIcon("/icons/MayaCharm_ToolWindow.png");
    }

    @NotNull
    @Override
    public String getId() {
        return "MAYACHARM_DEBUG_CONFIGURATION";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] {new MayaCharmDebugConfigFactory(this)};
    }
}
