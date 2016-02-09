package ca.rightsomegoodgames.mayacharm.run;

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommInterface;
import ca.rightsomegoodgames.mayacharm.run.MayaCharmRunProfile;
import ca.rightsomegoodgames.mayacharm.settings.MCSettingsProvider;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.GenericProgramRunner;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
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
        return runProfile instanceof MayaCharmRunProfile && s.equals("Run");
    }

    @Nullable
    @Override
    protected RunContentDescriptor doExecute(@NotNull Project project, @NotNull RunProfileState state,
                                             @Nullable RunContentDescriptor contentToReuse, @NotNull ExecutionEnvironment environment) throws ExecutionException {
        final MayaCharmRunProfile configuration = (MayaCharmRunProfile) environment.getRunProfile();
        final MCSettingsProvider settingsProvider = MCSettingsProvider.getInstance(project);
        final MayaCommInterface mayaCommInterface = new MayaCommInterface(settingsProvider.getHost(), settingsProvider.getPort());

        ToolWindow mayaLogWindow = ToolWindowManager.getInstance(project).getToolWindow("Maya Log");
        mayaLogWindow.activate(null, true, true);

        mayaCommInterface.connectMayaLog();
        if (configuration.getUseCode()) {
            mayaCommInterface.sendCodeToMaya(configuration.getScriptCodeText());
        }
        else {
            mayaCommInterface.sendFileToMaya(configuration.getScriptFilePath());
        }
        return null;
    }
}
