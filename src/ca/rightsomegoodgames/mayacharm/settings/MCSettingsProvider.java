package ca.rightsomegoodgames.mayacharm.settings;

import com.intellij.openapi.components.*;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nullable;

@State(
        name = "MCSettingsProvider",
        storages = {
                @Storage(file = StoragePathMacros.WORKSPACE_FILE)
        }
)

public class MCSettingsProvider implements PersistentStateComponent<MCSettingsProvider.State> {
    private State myState = new State();

    private static final int DEFUALT_PORT = 4434;
    private static final String DEFUALT_HOSTNAME = "localhost";

    public void setPort(int port) {
        myState.Port = (port == -1 || port == 0) ? DEFUALT_PORT : port;
    }

    public int getPort() {
        return (myState.Port == -1 || myState.Port == 0) ? DEFUALT_PORT : myState.Port;
    }

    public void setHost(String host) {
        myState.Host = (host.isEmpty()) ? DEFUALT_HOSTNAME : host;
    }

    public String getHost() {
        return myState.Host.isEmpty() ? DEFUALT_HOSTNAME : myState.Host;
    }

    public String getFilePath() {
        return myState.FilePath;
    }

    public void setFilePath(String path) {
        myState.FilePath = path;
    }

    public String getCodeSnippet() {
        return myState.CodeSnippet;
    }

    public void setCodeSnippet(String snippet) {
        myState.CodeSnippet = snippet;
    }

    public static MCSettingsProvider getInstance(Project project) {
        return ServiceManager.getService(project, MCSettingsProvider.class);
    }

    @Nullable
    @Override
    public State getState() {
        return myState;
    }

    @Override
    public void loadState(State state) {
        myState.Host = state.Host;
        myState.Port = state.Port;
        myState.FilePath = state.FilePath;
        myState.CodeSnippet = state.CodeSnippet;
    }

    public static class State {
        public int Port;
        public String Host;
        public String FilePath;
        public String CodeSnippet;
    }
}
