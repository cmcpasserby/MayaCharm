package ca.rightsomegoodgames.mayacharm.logconsole

import ca.rightsomegoodgames.mayacharm.mayacomms.LOG_FILENAME_STRING
import ca.rightsomegoodgames.mayacharm.settings.ProjectSettings
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.ui.content.ContentFactory
import java.io.File
import java.nio.charset.Charset

class LogWindow : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val port = ProjectSettings.getInstance(project).selectedSdk?.port ?: 4434

        val contentManager = toolWindow.contentManager
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val mayaLogPath = PathManager.getPluginTempPath() + String.format(LOG_FILENAME_STRING, port)

        val console = LogConsole(
                project,
                File(mayaLogPath),
                Charset.defaultCharset(),
                0L,
                "MayaLog",
                false,
                GlobalSearchScope.allScope(project)
        )

        val content = contentFactory.createContent(console.component, "", false)
        contentManager.addContent(content)

        toolWindow.setAvailable(true, null)
        toolWindow.isToHideOnEmptyContent = true
        toolWindow.activate(console::activate)
    }
}
