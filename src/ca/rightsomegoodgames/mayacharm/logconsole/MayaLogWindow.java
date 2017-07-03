package ca.rightsomegoodgames.mayacharm.logconsole;

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommInterface;
import ca.rightsomegoodgames.mayacharm.settings.MCSettingsProvider;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.nio.charset.Charset;

public class MayaLogWindow implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow toolWindow) {
        final MCSettingsProvider settings = MCSettingsProvider.getInstance(project);

        final ContentManager contentManager = toolWindow.getContentManager();
        final ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        final String mayaLogPath = PathManager.getPluginTempPath()
                + String.format(MayaCommInterface.LOG_FILENAME_STRING, settings.getPort());

        final MayaLogConsole console = new MayaLogConsole(project, new File(mayaLogPath),
                Charset.defaultCharset(), 0L, "MayaLog", false, GlobalSearchScope.allScope(project));

        final Content content = contentFactory.createContent(console.getComponent(), "", false);
        contentManager.addContent(content);

        toolWindow.setAvailable(true, null);
        toolWindow.setToHideOnEmptyContent(true);
        toolWindow.activate(console::activate);
    }
}
