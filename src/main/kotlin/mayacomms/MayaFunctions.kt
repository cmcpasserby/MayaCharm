package mayacomms

import com.intellij.openapi.util.SystemInfo
import com.intellij.util.io.exists
import java.nio.file.Paths

private const val mayaExecutableNameWin = "maya.exe"
private const val mayaPy2ExecutableNameWin = "mayapy2.exe"
private const val mayaPy3ExecutableNameWin = "mayapy.exe"

private const val mayaExecutableNameMac = "Maya"
private const val mayaPy2ExecutableNameMac = "mayapy2"
private const val mayaPy3ExecutableNameMac = "mayapy"

private const val mayaExecutableNameLinux = "maya"
private const val mayaPy2ExecutableNameLinux = "mayapy2"
private const val mayaPy3ExecutableNameLinux = "mayapy"

private val PyVersionRe = """Python (\d+\.\d+\.\d+)""".toRegex()

enum class MayaPyVersion {
    None,
    Py2,
    Py3,
}

val mayaExecutableName: String
    get() = when {
        SystemInfo.isWindows -> mayaExecutableNameWin
        SystemInfo.isMac -> mayaExecutableNameMac
        SystemInfo.isLinux -> mayaExecutableNameLinux
        else -> ""
    }

val mayaPy2ExecutableName: String
    get() = when {
        SystemInfo.isWindows -> mayaPy2ExecutableNameWin
        SystemInfo.isMac -> mayaPy2ExecutableNameMac
        SystemInfo.isLinux -> mayaPy2ExecutableNameLinux
        else -> throw Exception("Invalid Platform")
    }

val mayaPy3ExecutableName: String
    get() = when {
        SystemInfo.isWindows -> mayaPy3ExecutableNameWin
        SystemInfo.isMac -> mayaPy3ExecutableNameMac
        SystemInfo.isLinux -> mayaPy3ExecutableNameLinux
        else -> throw Exception("Invalid Platform")
    }


fun isValidMayaPy(path: String): Boolean = path.endsWith(mayaPy2ExecutableName) || path.endsWith(mayaPy3ExecutableName)

fun mayaPysFromMaya(path: String): Pair<String?, String?> {
    val p = Paths.get(path)

    if (p.fileName.toString() != mayaExecutableName) {
        return Pair(null, null)
    }

    if (SystemInfo.isMac) {
        val py2Path = p.parent.resolve("bin/$mayaPy2ExecutableName").let { if (it.exists()) it.toString() else null }
        val py3Path = p.parent.resolve("bin/$mayaPy3ExecutableName").let { if (it.exists()) it.toString() else null }
        return Pair(py2Path, py3Path)
    }

    if (SystemInfo.isWindows || SystemInfo.isLinux) {
        val py2Path = p.parent.resolve(mayaPy2ExecutableName).let { if (it.exists()) it.toString() else null }
        val py3Path = p.parent.resolve(mayaPy3ExecutableName).let { if (it.exists()) it.toString() else null }
        return Pair(py2Path, py3Path)
    }

    return Pair(null, null)
}

fun mayaFromMayaPy(path: String): String? {
    val p = Paths.get(path)

    if (p.fileName.toString() != mayaPy2ExecutableName || p.fileName.toString() != mayaPy3ExecutableName) {
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

fun getMayaPyVersion(versionString: String): MayaPyVersion {
    val ver = PyVersionRe.find(versionString)?.value ?: return MayaPyVersion.None
    return when(ver.split('.')[0]) {
        "2" -> MayaPyVersion.Py2
        "3" -> MayaPyVersion.Py3
        else -> MayaPyVersion.None
    }
}
