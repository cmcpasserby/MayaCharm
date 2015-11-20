package ca.rightsomegoodgames.mayacharm.settings;

import com.intellij.openapi.Disposable;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

public class MayaCharmConfig implements Configurable, SearchableConfigurable, Configurable.NoScroll, Disposable {
    public static final String CONSOLE_SETTINGS_HELP_REFERENCE = "reference.settings.ssh.terminal";
    private MCSettingsPanel myPanel;
    private final MCSettingsProvider mcSettingsProvider;
    private Project project;

    public MayaCharmConfig(Project project) {
        this.project = project;
        mcSettingsProvider = MCSettingsProvider.getInstance(project);
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "MayaCharm";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return CONSOLE_SETTINGS_HELP_REFERENCE;
    }

    @Override
    public void dispose() {
        myPanel = null;
    }

    @NotNull
    @Override
    public String getId() {
        return "mayacharm";
    }

    @Nullable
    @Override
    public Runnable enableSearch(String s) {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        myPanel = new MCSettingsPanel(mcSettingsProvider, project);
        return myPanel.createPanel();
    }

    @Override
    public boolean isModified() {
        return myPanel.isModified();
    }

    @Override
    public void apply() throws ConfigurationException {
        myPanel.apply();
    }

    @Override
    public void reset() {
        myPanel.reset();
    }

    @Override
    public void disposeUIResources() {
        Disposer.dispose(this);
    }
}
