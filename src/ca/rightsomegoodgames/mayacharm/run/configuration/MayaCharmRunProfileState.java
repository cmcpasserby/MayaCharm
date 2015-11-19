package ca.rightsomegoodgames.mayacharm.run.configuration;

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommInterface;
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

    public MayaCharmRunProfileState(ExecutionEnvironment executionEnvironment, Project project) {
        this.executionEnvironment = executionEnvironment;
        this.project = project;
    }

    @Nullable
    @Override
    public ExecutionResult execute(Executor executor, @NotNull ProgramRunner programRunner) throws ExecutionException {
        MayaCommInterface mayaPipe = new MayaCommInterface("localHost", 12345);
        return null;
    }
}
