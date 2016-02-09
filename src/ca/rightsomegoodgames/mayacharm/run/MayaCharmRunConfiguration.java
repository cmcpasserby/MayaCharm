package ca.rightsomegoodgames.mayacharm.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.SkipDefaultsSerializationFilter;
import com.intellij.util.xmlb.XmlSerializer;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;


public class MayaCharmRunConfiguration extends RunConfigurationBase implements MayaCharmRunProfile {
    private String scriptFilePath;
    private String scriptCodeText;
    private boolean useCode;

    public static final SkipDefaultsSerializationFilter SERIALIZATION_FILTERS = new SkipDefaultsSerializationFilter();

    public MayaCharmRunConfiguration(Project project, MayaCharmConfigurationFactory configFactory, String name) {
        super(project, configFactory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new MayaCharmConfigEditor(this);
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        ConfigurationState state = XmlSerializer.deserialize(element, ConfigurationState.class);
        if (state != null) {
            scriptFilePath = state.ScriptFilePath;
            scriptCodeText = state.ScriptCodeText;
            useCode = state.UseCode;
        }
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        ConfigurationState state = new ConfigurationState();
        state.ScriptFilePath = scriptFilePath;
        state.ScriptCodeText = scriptCodeText;
        state.UseCode = useCode;

        XmlSerializer.serializeInto(state, element, SERIALIZATION_FILTERS);
        super.writeExternal(element);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        if (getUseCode()) {
            if (scriptCodeText == null || scriptCodeText.isEmpty())
                throw new RuntimeConfigurationException("Code field is empty!");
        }
        else {
            if (scriptFilePath == null || scriptFilePath.isEmpty() || !new File(scriptFilePath).isFile())
                throw new RuntimeConfigurationException("File does not exist!");
        }
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return (executor1, programRunner) -> null;
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

    public static class ConfigurationState {
        public String ScriptFilePath;
        public String ScriptCodeText;
        public boolean UseCode;
    }
}
