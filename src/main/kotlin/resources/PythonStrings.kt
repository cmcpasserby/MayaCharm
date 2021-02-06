package resources

import java.text.MessageFormat

enum class PythonStrings(val message: String) {
    UTF8_ENCODING_STR("# -*- coding: utf-8 -*-"),
    OPEN_LOG("import maya.cmds as cmds; cmds.cmdFileOutput(o=r\"{0}\")"),
    CLOSE_LOG("import maya.cmds as cmds; cmds.cmdFileOutput(closeAll=True)"),
    EXECFILE("python (\"execfile (\\\"{0}\\\")\");"),
    PYSTDERR("# Error: "),
    PYSTDWRN("# Warning: "),
    SETTRACE("import pydevd; pydevd.settrace(host=\"{0}\", port={1,number,#}, suspend={2}, stdoutToServer={3}, stderrToServer={3})"),
    STOPTRACE("import pydevd; pydevd.stoptrace()"),
    CMDPORTSETUPSCRIPT(this::class.java.classLoader.getResource("python/command_port_setup.py")?.readText() ?: "");

    fun format(vararg args: Any): String = MessageFormat.format(message, *args)
}
