package ca.rightsomegoodgames.mayacharm.logconsole

import ca.rightsomegoodgames.mayacharm.mayacomms.LOG_FILENAME_STRING
import ca.rightsomegoodgames.mayacharm.resources.PythonStrings
import ca.rightsomegoodgames.mayacharm.settings.ProjectSettings
import com.intellij.diagnostic.logging.LogConsoleImpl
import com.intellij.diagnostic.logging.LogFragment
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import java.io.File
import java.io.PrintWriter
import java.nio.charset.Charset

class LogConsole(private val project: Project, file: File, charset: Charset, skippedContents: Long, title: String, buildInActions: Boolean, searchScope: GlobalSearchScope?)
    : LogConsoleImpl(project, file, charset, skippedContents, title, buildInActions, searchScope) {

    init {
        super.setContentPreprocessor(fun(it: String): MutableList<LogFragment> {
            val lFrag = mutableListOf<LogFragment>()
            val checks = it.startsWith(PythonStrings.INSTANCE.PYSTDERR) || it.startsWith(PythonStrings.INSTANCE.PYSTDWRN)
            val outType = if (checks) ProcessOutputTypes.STDERR else ProcessOutputTypes.STDOUT
            lFrag.add(LogFragment(it, outType))
            return lFrag
        })
    }

    override fun isActive(): Boolean {
        return true
    }

    override fun clear() {
        super.clear()
        val sdk = ProjectSettings.getInstance(project).selectedSdk ?: return

        val mayaLogPath = PathManager.getPluginTempPath() + String.format(LOG_FILENAME_STRING, sdk.port)

        var writer: PrintWriter? = null

        try {
            writer = PrintWriter(mayaLogPath)
            writer.print("")
            writer.close()
        }
        finally {
            writer?.close()
        }
    }
}
