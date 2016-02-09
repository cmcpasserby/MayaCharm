package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.SkipDefaultsSerializationFilter;
import com.intellij.util.xmlb.XmlSerializer;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfiguration;
import org.jdom.Element;
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
    public void readExternal(Element element) throws InvalidDataException {
//        super.readExternal(element);
        ConfigurationState state = XmlSerializer.deserialize(element, ConfigurationState.class);
        if (state != null) {
            scriptFilePath = state.ScriptFilePath;
            scriptCodeText = state.ScriptCodeText;
            useCode = state.IsUseCode;
            setHost(state.Host);
            setPort(state.Port);
            setRedirectOutput(state.IsRedirectOutput);
            setSuspendAfterConnect(state.IsSuspend);
        }
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        ConfigurationState state = new ConfigurationState();
        state.ScriptFilePath = scriptFilePath;
        state.ScriptCodeText = scriptCodeText;
        state.IsUseCode = useCode;
        state.Host = getHost();
        state.Port = getPort();
        state.IsRedirectOutput = isRedirectOutput();
        state.IsSuspend = isSuspendAfterConnect();

        XmlSerializer.serializeInto(state, element, SERIALIZATION_FILTER);
//        super.writeExternal(element);
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

    public static class ConfigurationState {
        public String ScriptFilePath;
        public String ScriptCodeText;
        public boolean IsUseCode;
        public String Host;
        public int Port;
        public boolean IsRedirectOutput;
        public boolean IsSuspend;
    }
}
