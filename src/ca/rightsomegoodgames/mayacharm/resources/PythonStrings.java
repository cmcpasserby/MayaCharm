package ca.rightsomegoodgames.mayacharm.resources;

import com.intellij.openapi.application.PathManager;
import org.jetbrains.annotations.NonNls;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public final class PythonStrings {
    public static final String OPEN_LOG = "import maya.cmds as cmds; cmds.cmdFileOutput(o=r\"{0}\")";
    public static final String CLOSE_LOG = "import maya.cmds as cmds; cmds.cmdFileOutput(closeAll=True)";
    public static final String EXECFILE = "python (\"execfile (\\\"{0}\\\")\");";

    public static final String SETTRACE = "import pydevd; pydevd.settrace(\"%1$s\", port=%2$s, suspend=%3$s, stdoutToServer=%4$s, stderrToServer=%4$s)";
    public static final String STOPTRACE = "import pydevd; pydevd.stoptrace()";

    public static final String PYSTDERR = "# Error: ";
    public static final String PYSTDWRN = "# Warning: ";

    /**
     * Unformatted script to open a command port in Maya, if not already opened.
     */
    public static final String CMDPORT_SETUP_SCRIPT = readResourceTextFile("python/command_port_setup.py");

    /**
     * Final, formatted pydevd setup script to append {@code pycharm-debug.egg} to {@code sys.path}.
     */
    public static final String PYDEV_SETUP_SCRIPT = String.format(
            readResourceTextFile("python/pydev_setup.py"),
            Paths.get(PathManager.getHomePath(), "debug-eggs", "pycharm-debug.egg").toString()
    );

    /**
     * Read a resource text file contents into a string.
     *
     * If it fails to read, an empty string is returned.
     *
     * @param resource Name of the resource text file.
     * @return Contents of the resource text file as a string.
     */
    private static String readResourceTextFile(@NonNls String resource) {
        URL pydev_setup_resource = PythonStrings.class.getClassLoader().getResource(resource);
        String fileContents = "";
        try {
            URI pydev_setup_uri = pydev_setup_resource.toURI();
            byte[] bytes = Files.readAllBytes(Paths.get(pydev_setup_uri));
            fileContents = new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException | URISyntaxException | NullPointerException e) {
            e.printStackTrace();
        }
        return fileContents;
    }
}
