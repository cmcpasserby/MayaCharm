package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfiguration;
import org.jetbrains.annotations.NotNull;

public class MayaCharmDebugConfig extends PyRemoteDebugConfiguration {

    public MayaCharmDebugConfig(Project project, MayaCharmDebugConfigFactory configurationFactory, String s) {
        super(project, configurationFactory, s);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MayaCharmDebugEditor(getProject(), this);
    }

    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return new MayaCharmDebugProfileState(getProject(), executionEnvironment);
    }
}
