package ca.rightsomegoodgames.mayacharm.run.debug;

import ca.rightsomegoodgames.mayacharm.run.MayaCharmRunProfile;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.util.xmlb.SkipDefaultsSerializationFilter;
import com.intellij.util.xmlb.XmlSerializer;
import com.jetbrains.python.debugger.remote.PyRemoteDebugConfiguration;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;

import java.io.File;


public class MayaCharmDebugConfig extends PyRemoteDebugConfiguration implements MayaCharmRunProfile {
    private static final SkipDefaultsSerializationFilter SERIALIZATION_FILTER = new SkipDefaultsSerializationFilter();
    private String scriptFilePath;
    private String scriptCodeText;
    private ExecutionType executionType = ExecutionType.DEBUG;

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
            executionType = state.ExecutionType;
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
        state.ExecutionType = executionType;
        state.Host = getHost();
        state.Port = getPort();
        state.IsRedirectOutput = isRedirectOutput();
        state.IsSuspend = isSuspendAfterConnect();

        XmlSerializer.serializeInto(state, element, SERIALIZATION_FILTER);
//        super.writeExternal(element);
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        super.checkConfiguration();

        switch (getExecutionType()) {
            case FILE:
                if (scriptFilePath == null || scriptFilePath.isEmpty() || !new File(scriptFilePath).isFile())
                    throw new RuntimeConfigurationException("File does not exist!");
                break;
            case CODE:
                if (scriptCodeText == null || scriptCodeText.isEmpty())
                    throw new RuntimeConfigurationException("Code field is empty!");
                break;
        }
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

    public ExecutionType getExecutionType() {
        return executionType;
    }

    public void setExecutionType(ExecutionType executionType) {
        this.executionType = executionType;
    }

    @Override
    public int getPort() {
        int port = super.getPort();
        return (port == 0 || port == -1) ? 60059 : port;
    }

    public enum ExecutionType {
        DEBUG, FILE, CODE
    }

    public static class ConfigurationState {
        public String ScriptFilePath;
        public String ScriptCodeText;
        public ExecutionType ExecutionType = MayaCharmDebugConfig.ExecutionType.DEBUG;
        public String Host;
        public int Port;
        public boolean IsRedirectOutput;
        public boolean IsSuspend;
    }
}
