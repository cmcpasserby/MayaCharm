package logconsole

import com.intellij.diagnostic.logging.DefaultLogFilterModel
import mayacomms.LOG_FILENAME_STRING
import resources.PythonStrings
import settings.ProjectSettings
import com.intellij.diagnostic.logging.LogConsoleImpl
import com.intellij.execution.process.ProcessOutputTypes
import com.intellij.openapi.application.PathManager
import com.intellij.openapi.project.Project
import com.intellij.psi.search.GlobalSearchScope
import java.io.File
import java.io.PrintWriter
import java.nio.charset.Charset

class LogConsole(
    private val project: Project,
    file: File,
    charset: Charset,
    skippedContents: Long,
    title: String,
    buildInActions: Boolean,
    searchScope: GlobalSearchScope?
) : LogConsoleImpl(project, file, charset, skippedContents, title, buildInActions, searchScope) {

    init {
        super.setFilterModel(object : DefaultLogFilterModel(project) {
            override fun processLine(line: String?): MyProcessingResult {
                line ?: return MyProcessingResult(ProcessOutputTypes.STDOUT, false, null)
                val checks = line.startsWith(PythonStrings.PYSTDERR.message) || line.startsWith(PythonStrings.PYSTDWRN.message)
                val outType = if (checks) ProcessOutputTypes.STDERR else ProcessOutputTypes.STDOUT
                return MyProcessingResult(outType, true, null)
            }
        })
    }

    override fun isActive(): Boolean = true

    override fun clear() {
        super.clear()
        val sdk = ProjectSettings.getInstance(project).selectedSdk ?: return

        val mayaLogPath = PathManager.getPluginTempPath() + String.format(LOG_FILENAME_STRING, sdk.port)

        var writer: PrintWriter? = null

        try {
            writer = PrintWriter(mayaLogPath)
            writer.print("")
            writer.close()
        } finally {
            writer?.close()
        }
    }
}
