package ca.rightsomegoodgames.mayacharm.run.configuration;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MayaCharmRunConfiguration extends RunConfigurationBase {
    private String scriptFilePath;
    private String scriptCodeText;
    private boolean useCode;

    public MayaCharmRunConfiguration(Project project, MayaCharmConfigurationFactory configFactory, String name) {
        super(project, configFactory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MayaCharmConfigEditor(this);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {

    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return new MayaCharmRunProfileState(executionEnvironment, getProject());
    }

    public String getScriptFilePath() {
        return scriptFilePath;
    }

    public void setScriptFilePath(String scriptFilePath) {
        this.scriptFilePath = scriptFilePath;
    }

    public String getScriptCodeText() {
        return scriptCodeText;
    }

    public void setScriptCodeText(String scriptCodeText) {
        this.scriptCodeText = scriptCodeText;
    }

    public boolean getUseCode() {
        return useCode;
    }

    public void setUseCode(boolean useCode) {
        this.useCode = useCode;
    }
}
