package ca.rightsomegoodgames.mayacharm.mayacomms;

import ca.rightsomegoodgames.mayacharm.settings.MCSettingsProvider;
import com.intellij.notification.Notifications;
import ca.rightsomegoodgames.mayacharm.resources.MayaNotifications;
import ca.rightsomegoodgames.mayacharm.resources.PythonStrings;
import com.intellij.openapi.application.PathManager;

import java.io.*;
import java.net.Socket;
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

    public void sendToMaya(String message) {
        File file = writeFile(message);
        sendToPort(file);
    }

    public void connectMayaLog() {
        final String mayaLogPath = PathManager.getPluginTempPath() + "/mayalog.txt";
        final String message = MessageFormat.format(PythonStrings.CONNECT_LOG, mayaLogPath);

        try {
            createMayaLog(mayaLogPath);
            sendToMaya(message);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createMayaLog(String path) throws IOException {
        final File mayaLog = new File(path);
        if (!mayaLog.exists()) {
            mayaLog.createNewFile();
        }
        return mayaLog;
    }
}
