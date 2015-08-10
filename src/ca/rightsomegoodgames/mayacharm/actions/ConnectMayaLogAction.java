package ca.rightsomegoodgames.mayacharm.actions;

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommInterface;
import ca.rightsomegoodgames.mayacharm.settings.MCSettingsProvider;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

public class ConnectMayaLogAction extends BaseSendAction {
    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(true);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final MCSettingsProvider settings = MCSettingsProvider.getInstance(e.getProject());
        MayaCommInterface maya = new MayaCommInterface(settings.getHost(), settings.getPort());
        maya.connectMayaLog();
    }
}
