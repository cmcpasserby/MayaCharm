package ca.rightsomegoodgames.mayacharm.run;

import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class MayaCharmConfigurationFactory extends ConfigurationFactory {
    private static final String FACTORY_NAME = "MayaCharm Configuration Factory";

    public MayaCharmConfigurationFactory(MayaCharmConfigurationType type) {
        super(type);
    }

    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new MayaCharmRunConfiguration(project, this, "MayaCharm");
    }

    public String getName() {
        return FACTORY_NAME;
    }
}
