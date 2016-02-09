package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.SkipDefaultsSerializationFilter;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfiguration;
import org.jetbrains.annotations.NotNull;


public class MayaCharmDebugConfig extends PyRemoteDebugConfiguration {
    private static final SkipDefaultsSerializationFilter SERIALIZATION_FILTER = new SkipDefaultsSerializationFilter();
    private String scriptFilePath;
    private String scriptCodeText;
    private boolean useCode;

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

    public boolean isUseCode() {
        return useCode;
    }

    public void setUseCode(boolean useCode) {
        this.useCode = useCode;
    }
}
