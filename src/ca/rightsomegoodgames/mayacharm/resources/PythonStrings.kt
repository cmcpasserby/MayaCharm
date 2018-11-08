package ca.rightsomegoodgames.mayacharm.resources

import com.intellij.openapi.application.PathManager
import java.nio.file.Paths

class PythonStrings {
    val OPEN_LOG: String = "import maya.cmds as cmds; cmds.cmdFileOutput(o=r\"{0}\")"
    val CLOSE_LOG: String = "import maya.cmds as cmds; cmds.cmdFileOutput(closeAll=True)"
    val EXECFILE: String = "python (\"execfile (\\\"{0}\\\")\");"

    val PYSTDERR: String = "# Error: "
    val PYSTDWRN: String = "# Warning: "

    private val _cmdportSetupScript: String
    private val _pydevSetupScript: String

    val cmdportSetupScript: String
        get() = _cmdportSetupScript

    val pyDevdSetupScript: String
        get() = _pydevSetupScript

    init {
        _cmdportSetupScript = readStringFromResource("python/command_port_setup.py")
        val eggPath = Paths.get(PathManager.getHomePath(), "debug-eggs", "pycharm-debug.egg").toString()
        _pydevSetupScript = String.format(readStringFromResource("python/pydev_setup.py"), eggPath)
    }

    companion object {
        @JvmField
        val INSTANCE: PythonStrings = PythonStrings()
        fun readStringFromResource(resource: String): String {
            return PythonStrings::class.java.classLoader.getResource(resource).readText()
        }
    }
}
