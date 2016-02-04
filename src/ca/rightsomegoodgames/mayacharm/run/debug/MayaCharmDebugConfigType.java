package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfigurationType;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaCharmDebugConfigType extends PyRemoteDebugConfigurationType {
    @Override
    public String getDisplayName() {
        return "MayaCharmDebugger";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "MayaCharm Remote Debugger Configuration Type";
    }

    @Override
    public Icon getIcon() {
        return super.getIcon();
    }

    @NotNull
    @Override
    public String getId() {
        return "MAYACHARM_DEBUG_CONFIGURATION";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return super.getConfigurationFactories();
    }
}
