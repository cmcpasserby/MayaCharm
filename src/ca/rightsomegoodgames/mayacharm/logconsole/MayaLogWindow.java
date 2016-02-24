package ca.rightsomegoodgames.mayacharm.logconsole;

import com.intellij.diagnostic.logging.DefaultLogFilterModel;
import com.intellij.diagnostic.logging.LogConsoleBase;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.content.ContentManager;
import org.jetbrains.annotations.NotNull;

import java.io.*;

public class MayaLogWindow implements ToolWindowFactory {
    @Override
    public void createToolWindowContent(@NotNull final Project project, @NotNull ToolWindow toolWindow) {
        final ContentManager contentManager = toolWindow.getContentManager();
        final ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        final String mayaLogPath = PathManager.getPluginTempPath() + "/mayaLog.txt";

//        final MayaLogConsole console = new MayaLogConsole(project, new File(mayaLogPath),
//                Charset.defaultCharset(), 0L, "MayaLog", false, GlobalSearchScope.allScope(project));

        InputStreamReader reader = null;
        try {
            reader = new InputStreamReader(new FileInputStream(mayaLogPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        final LogConsoleBase console = new LogConsoleBase(project, reader, "Maya Log", false, new DefaultLogFilterModel(project)) {
            @Override
            public void activate() {
                super.activate();
            }

            @Override
            public boolean isActive() {
                return true;
            }
        };

        final Content content = contentFactory.createContent(console.getComponent(), "", false);
        contentManager.addContent(content);

        toolWindow.setAvailable(true, null);
        toolWindow.setToHideOnEmptyContent(true);
        toolWindow.activate(console::activate);
    }
}
