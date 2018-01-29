package ca.rightsomegoodgames.mayacharm.resources;

public final class PythonStrings {
    public static final String CONNECT_LOG = "import maya.cmds as cmds; cmds.cmdFileOutput(o=r\"{0}\")";
    public static final String EXECFILE = "python (\"execfile (\\\"{0}\\\")\");";

    public static final String SETTRACE = "import pydevd; pydevd.settrace(\"localhost\", port=%1$s, suspend=%2$s, stdoutToServer=%3$s, stderrToServer=%3$s)";
    public static final String STOPTRACE = "import pydevd; pydevd.stoptrace()";

    public static final String PYSTDERR = "# Error: ";
    public static final String PYSTDWRN = "# Warning: ";
}
