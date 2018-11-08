package ca.rightsomegoodgames.mayacharm.mayacomms

import ca.rightsomegoodgames.mayacharm.resources.MayaNotifications
import ca.rightsomegoodgames.mayacharm.resources.PythonStrings
import com.intellij.notification.Notifications
import com.intellij.openapi.application.PathManager
import java.io.*
import java.net.Socket
import java.text.MessageFormat

const val LOG_FILENAME_STRING: String = "/mayalog%s.txt"

class MayaCommandInterface(private val host: String, private val port: Int) {
    private val logFileName: String = String.format(LOG_FILENAME_STRING, port)

    private fun writeFile (text: String): File? {
        var tempFile: File? = null
        val bw: BufferedWriter

        try {
            tempFile = File.createTempFile("MayaCharmTemp", ".py")
            if (!tempFile.exists()) {
                tempFile.createNewFile()
            }

            bw = BufferedWriter(FileWriter(tempFile))
            bw.write(text)
            bw.close()
            tempFile.deleteOnExit()
        }
        catch (e: IOException) {
            Notifications.Bus.notify(MayaNotifications.FILE_FAIL)
            e.printStackTrace()
        }
        return tempFile
    }

    private fun sendToPort(message: File) {
        var client: Socket? = null
        var out: PrintWriter? = null

        try {
            client = Socket(host, port)
            out = PrintWriter(client.getOutputStream(), true)
            val outString = MessageFormat.format(PythonStrings.INSTANCE.EXECFILE, message.toString().replace("\\", "/"))
            out.println(outString)
        }
        catch (e: IOException) {
            Notifications.Bus.notify(MayaNotifications.CONNECTION_REFUSED)
            e.printStackTrace()
        }
        finally {
            out?.close()
            client?.close()
        }
    }

    public fun sendCodeToMaya(message: String) {
        val file = writeFile(message)
        sendToPort(file!!)
    }

    public fun sendFileToMaya(path: String) {
        val file = File(path)
        sendToPort(file)
    }

    public fun connectMayaLog() {
        val mayaLogPath = PathManager.getPluginTempPath() + logFileName
        var message = PythonStrings.INSTANCE.CLOSE_LOG
        message += System.lineSeparator() + MessageFormat.format(PythonStrings.INSTANCE.OPEN_LOG, mayaLogPath)
        createMayaLog(mayaLogPath)
        sendCodeToMaya(message)
    }

    public fun pyDevSetup() {
        sendCodeToMaya(PythonStrings.INSTANCE.pyDevdSetupScript)
    }

    private fun createMayaLog(path: String): File {
        val mayaLog = File(path)
        if (!mayaLog.exists()) {
            mayaLog.createNewFile()
        }
        return mayaLog
    }
}
