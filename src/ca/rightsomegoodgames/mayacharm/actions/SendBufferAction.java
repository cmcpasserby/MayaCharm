package ca.rightsomegoodgames.mayacharm.actions;

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommInterface;
import ca.rightsomegoodgames.mayacharm.settings.MCSettingsProvider;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.vfs.VirtualFile;
import org.jetbrains.annotations.NotNull;


public class SendBufferAction extends BaseSendAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final MCSettingsProvider settings = MCSettingsProvider.getInstance(e.getProject());
        final VirtualFile data = e.getData(LangDataKeys.VIRTUAL_FILE);
        if (data != null) {
            MayaCommInterface maya = new MayaCommInterface(settings.getHost(), settings.getPort());
            maya.sendFileToMaya(data.getPath());
        }
    }
}
