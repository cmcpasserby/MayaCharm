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

private fun pathForPidUnix(pid: Int): String? = try {
    ProcessBuilder("readlink -f /proc/$pid/exe".split("\\s".toRegex()))
        .redirectOutput(ProcessBuilder.Redirect.PIPE)
        .redirectError(ProcessBuilder.Redirect.PIPE)
        .start().apply { waitFor(timeout.first, timeout.second) }
        .inputStream.bufferedReader().readText().trim()
} catch (err: IOException) {
    err.printStackTrace()
    null
}

fun pathForPid(pid: Int): String? = if (SystemInfo.isWindows) {
    pathForPidWin(pid)
} else {
    pathForPidUnix(pid)
}
