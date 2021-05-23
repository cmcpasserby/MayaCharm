package utils

import com.intellij.openapi.util.SystemInfo
import java.io.IOException
import java.util.concurrent.TimeUnit

private val timeout: Pair<Long, TimeUnit> = Pair(10, TimeUnit.SECONDS)

private fun pathForPidWin(pid: Int): String? = try {
    ProcessBuilder("wmic process where 'ProcessID=$pid' get ExecutablePath".split("\\s".toRegex()))
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start().apply { waitFor(timeout.first, timeout.second) }
        .inputStream.bufferedReader().readText().lines()[2].trim()
} catch (err: IOException) {
    err.printStackTrace()
    null
}

private fun pathForPidMac(pid: Int): String? = try {
    ProcessBuilder("ps -p $pid".split("\\s".toRegex()))
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start().apply { waitFor(timeout.first, timeout.second) }
        .inputStream.bufferedReader().readText()
        .lines()[1].let { it.substring(it.indexOf('/')) }
} catch (err: IOException) {
    err.printStackTrace()
    null
}

private fun pathForPidUnix(pid: Int): String? = try {
    ProcessBuilder("readlink", "/proc/$pid/exe")
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start().apply { waitFor(timeout.first, timeout.second) }
        .inputStream.bufferedReader().readText().trim()
} catch (err: IOException) {
    err.printStackTrace()
    null
}

fun pathForPid(pid: Int): String? = when {
    SystemInfo.isWindows -> pathForPidWin(pid)
    SystemInfo.isMac -> pathForPidMac(pid)
    else -> pathForPidUnix(pid)
}
