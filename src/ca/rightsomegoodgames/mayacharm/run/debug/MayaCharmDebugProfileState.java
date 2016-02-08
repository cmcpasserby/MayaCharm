package ca.rightsomegoodgames.mayacharm.run.debug;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.Executor;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.openapi.project.Project;
import com.jetbrains.python.debugger.remote.PyRemoteDebugCommandLineState;
import org.jetbrains.annotations.NotNull;

public class MayaCharmDebugProfileState extends PyRemoteDebugCommandLineState {
    private final Project project;
    private final  ExecutionEnvironment executionEnvironment;

    public MayaCharmDebugProfileState(@NotNull Project project, @NotNull ExecutionEnvironment executionEnvironment) {
        super(project, executionEnvironment);
        this.project = project;
        this.executionEnvironment = executionEnvironment;
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        return super.startProcess();
    }

    @NotNull
    @Override
    public ExecutionResult execute(@NotNull Executor executor, @NotNull ProgramRunner runner) throws ExecutionException {
        return super.execute(executor, runner);
    }
}
