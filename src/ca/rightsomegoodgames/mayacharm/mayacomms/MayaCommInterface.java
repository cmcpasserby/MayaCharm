package ca.rightsomegoodgames.mayacharm.mayacomms;

import com.intellij.notification.Notifications;
import ca.rightsomegoodgames.mayacharm.resources.MayaNotifications;
import ca.rightsomegoodgames.mayacharm.resources.PythonStrings;
import com.intellij.openapi.application.PathManager;
import org.apache.commons.lang.CharSet;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;

public class MayaCommInterface {
    final private String host;
    final private int port;

    public MayaCommInterface(String host, int port) {
        this.host = host;
        this.port = port;
    }

    private File writeFile(String text) {
        File tempFile = null;
        BufferedWriter bw;

        try {
            tempFile = File.createTempFile("MayaCharmTemp", ".py");
            if (!tempFile.exists()) {
                //noinspection ResultOfMethodCallIgnored
                tempFile.createNewFile();
            }

            bw = new BufferedWriter(new FileWriter(tempFile));
            bw.write(text);
            bw.close();
            tempFile.deleteOnExit();
        }
        catch (IOException e) {
            Notifications.Bus.notify(MayaNotifications.FILE_FAIL);
            e.printStackTrace();
        }
        return tempFile;
    }

    private void sendToPort(File message) {
        Socket client = null;
        PrintWriter out = null;

        try {
            client = new Socket(host, port);
            out = new PrintWriter(client.getOutputStream(), true);

            String outString = MessageFormat.format(
                    PythonStrings.EXECFILE, message.toString().replace("\\", "/"));
            out.println(outString);
        }
        catch (IOException e) {
            Notifications.Bus.notify(MayaNotifications.CONNECTION_REFUSED);
            e.printStackTrace();
        }
        finally {
            if (out != null)
                out.close();
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void sendCodeToMaya(String message) {
        File file = writeFile(message);
        sendToPort(file);
    }

    public void sendFileToMaya(String path) {
        File file = new File(path);
        sendToPort(file);
    }

    public void pyDevSetup() {
        Path installDir = Paths.get(PathManager.getBinPath()).getParent();
        File debugEggPath = new File(installDir.toString(), "debug-eggs" + File.separator + "pycharm-debug.egg");
        ClassLoader classLoader = getClass().getClassLoader();
        File pydevSetupFile = new File(classLoader.getResource("python/pydev_setup.py").getFile());
        String lines = "";

        try {
            byte[] encodedLines = Files.readAllBytes(Paths.get(pydevSetupFile.toURI()));
            lines = new String(encodedLines, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        lines = String.format(lines, debugEggPath);
        sendCodeToMaya(lines);
    }

    public void setTrace(int port, boolean suspend, boolean print) {
        sendCodeToMaya(String.format(PythonStrings.SETTRACE, port, suspend ? "True" : "False", print ? "True" : "False"));
    }

    public void stopTrace() {
        sendCodeToMaya(PythonStrings.STOPTRACE);
    }

    public void connectMayaLog() {
        final String mayaLogPath = PathManager.getPluginTempPath() + "/mayalog.txt";
        final String message = MessageFormat.format(PythonStrings.CONNECT_LOG, mayaLogPath);

        try {
            createMayaLog(mayaLogPath);
            sendCodeToMaya(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createMayaLog(String path) throws IOException {
        final File mayaLog = new File(path);
        if (!mayaLog.exists()) {
            //noinspection ResultOfMethodCallIgnored
            mayaLog.createNewFile();
        }
        return mayaLog;
    }
}
