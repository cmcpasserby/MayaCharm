package ca.rightsomegoodgames.mayacharm.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaCharmConfigurationType implements ConfigurationType {
    @Override
    public String getDisplayName() {
        return "MayaCharm Runner";
    }

    @Override
    public String getConfigurationTypeDescription() {
        return "MayaCharm Run Configuration Type";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.FileTypes.Config;
    }

    @NotNull
    @Override
    public String getId() {
        return "MAYACHARM_RUN_CONFIGURATION";
    }

    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[] {new MayaCharmConfigurationFactory(this)};
    }
}
