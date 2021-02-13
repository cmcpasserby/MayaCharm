package mayacomms

import resources.MayaNotifications
import resources.PythonStrings
import com.intellij.notification.Notifications
import com.intellij.openapi.application.PathManager
import java.io.*
import java.net.Socket
import java.text.MessageFormat

const val LOG_FILENAME_STRING: String = "/mayalog%s.txt"

class MayaCommandInterface(private val port: Int) {
    private val logFileName: String = String.format(LOG_FILENAME_STRING, port)

    private fun writeFile (text: String): File? {
        var tempFile: File? = null
        val bw: BufferedWriter

        try {
            tempFile = File.createTempFile("MayaCharmTemp", ".py") ?: return null
            if (!tempFile.exists()) {
                tempFile.createNewFile()
            }

            bw = BufferedWriter(FileWriter(tempFile))
            bw.write(PythonStrings.UTF8_ENCODING_STR.message)
            bw.newLine()
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

        try {
            client = Socket("localhost", port)
            val outString = PythonStrings.EXECFILE.format(message.toString().replace("\\", "/"))
            client.outputStream.write(outString.toByteArray())
        }
        catch (e: IOException) {
            Notifications.Bus.notify(MayaNotifications.CONNECTION_REFUSED)
            e.printStackTrace()
        }
        finally {
            client?.close()
        }
    }

    fun sendCodeToMaya(message: String) {
        val file = writeFile(message)
        sendToPort(file!!)
    }

    fun sendFileToMaya(path: String) {
        val file = File(path)
        sendToPort(file)
    }

    fun connectMayaLog() {
        val mayaLogPath = PathManager.getPluginTempPath() + logFileName
        var message = PythonStrings.CLOSE_LOG.message
        message += System.lineSeparator() + PythonStrings.OPEN_LOG.format(mayaLogPath)
        createMayaLog(mayaLogPath)
        sendCodeToMaya(message)
    }

    private fun createMayaLog(path: String): File {
        val mayaLog = File(path)
        if (!mayaLog.exists()) {
            mayaLog.createNewFile()
        }
        return mayaLog
    }
}
