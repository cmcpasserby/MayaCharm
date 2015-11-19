package ca.rightsomegoodgames.mayacharm.run.configuration;

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommInterface;
import ca.rightsomegoodgames.mayacharm.settings.MCSettingsProvider;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MayaCharmRunProfileState implements RunProfileState {
    private final ExecutionEnvironment executionEnvironment;
    private final Project project;
    private final MCSettingsProvider settings;

    public MayaCharmRunProfileState(ExecutionEnvironment executionEnvironment, Project project) {
        this.executionEnvironment = executionEnvironment;
        this.project = project;
        settings = MCSettingsProvider.getInstance(project);
    }

    @Nullable
    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner programRunner) throws ExecutionException {
        final MayaCommInterface mayaPipe = new MayaCommInterface(settings.getHost(), settings.getPort());
        mayaPipe.sendToMaya("print 'ItWorks'");
        return null;
    }
}
