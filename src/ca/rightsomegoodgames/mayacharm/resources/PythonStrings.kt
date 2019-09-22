package ca.rightsomegoodgames.mayacharm.resources

class PythonStrings {
    val UTF8_ENCODING_STR: String = "# -*- coding: utf-8 -*-"
    val OPEN_LOG: String = "import maya.cmds as cmds; cmds.cmdFileOutput(o=r\"{0}\")"
    val CLOSE_LOG: String = "import maya.cmds as cmds; cmds.cmdFileOutput(closeAll=True)"
    val EXECFILE: String = "python (\"execfile (\\\"{0}\\\")\");"

    val PYSTDERR: String = "# Error: "
    val PYSTDWRN: String = "# Warning: "

    private val _cmdportSetupScript: String

    val cmdportSetupScript: String
        get() = this._cmdportSetupScript

    init {
        _cmdportSetupScript = readStringFromResource("python/command_port_setup.py")
    }

    companion object {
        @JvmField
        val INSTANCE: PythonStrings = PythonStrings()
        fun readStringFromResource(resource: String): String {
            return PythonStrings::class.java.classLoader.getResource(resource).readText()
        }
    }
}
