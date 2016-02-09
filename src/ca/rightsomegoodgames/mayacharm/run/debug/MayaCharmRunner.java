package ca.rightsomegoodgames.mayacharm.run.debug;

import ca.rightsomegoodgames.mayacharm.run.debug.MayaCharmDebugConfig;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.GenericProgramRunner;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class MayaCharmRunner extends GenericProgramRunner {
    @NotNull
    @Override
    public String getRunnerId() {
        return "MayaCharmRunner";
    }

    @Override
    public boolean canRun(@NotNull String s, @NotNull RunProfile runProfile) {
        return runProfile instanceof MayaCharmDebugConfig && s.equals("Run");
    }

    @Nullable
    @Override
    protected RunContentDescriptor doExecute(@NotNull Project project, @NotNull RunProfileState state, @Nullable RunContentDescriptor contentToReuse, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        MayaCharmDebugConfig config = (MayaCharmDebugConfig) environment.getRunProfile();
        return null;
    }
}
