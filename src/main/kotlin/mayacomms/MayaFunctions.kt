package mayacomms

import com.intellij.openapi.util.SystemInfo
import com.intellij.util.io.exists
import java.nio.file.Paths

private const val mayaExecutableNameWin = "maya.exe"
private const val mayaPyExecutableNameWin = "mayapy.exe"

private const val mayaExecutableNameMac = "Maya"
private const val mayaPyExecutableNameMac = "mayapy"

private const val mayaExecutableNameLinux = "maya"
private const val mayaPyExecutableNameLinux = "mayapy"

fun mayaPyFromMaya(path: String): String? {
    val p = Paths.get(path)

    if (p.fileName.toString() != mayaExecutableName) {
        return null
    }

    if (SystemInfo.isMac) {
        val newPath = p.parent.parent.resolve("bin/$mayaPyExecutableNameMac")
        return if (newPath.exists()) newPath.toString() else null
    }

    if (SystemInfo.isWindows || SystemInfo.isLinux) {
        val newPath = p.parent.resolve(mayaPyExecutableName)
        return if (newPath.exists()) newPath.toString() else null
    }

    return null
}

fun mayaFromMayaPy(path: String): String? {
    val p = Paths.get(path)

    if (p.fileName.toString() != mayaPyExecutableName) {
        return null
    }

    if (SystemInfo.isMac) {
        val newPath = p.parent.parent.resolve("MacOS/$mayaExecutableNameMac")
        return if (newPath.exists()) newPath.toString() else null
    }

    if (SystemInfo.isWindows || SystemInfo.isLinux) {
        val newPath = p.parent.resolve(mayaExecutableName)
        return if (newPath.exists()) newPath.toString() else null
    }

    return null
}

val mayaExecutableName: String
    get() {
        if (SystemInfo.isWindows) return mayaExecutableNameWin
        if (SystemInfo.isMac) return mayaExecutableNameMac
        if (SystemInfo.isLinux) return mayaExecutableNameLinux
        return ""
    }

val mayaPyExecutableName: String
    get() {
        if (SystemInfo.isWindows) return mayaPyExecutableNameWin
        if (SystemInfo.isMac) return mayaPyExecutableNameMac
        if (SystemInfo.isLinux) return mayaPyExecutableNameLinux
        return ""
    }
