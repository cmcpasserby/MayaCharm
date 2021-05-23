package flavors

import com.intellij.openapi.projectRoots.Sdk
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.python.psi.LanguageLevel
import com.jetbrains.python.sdk.flavors.PythonFlavorProvider
import com.jetbrains.python.sdk.flavors.PythonSdkFlavor
import icons.PythonIcons
import java.io.File
import javax.swing.Icon

class MayaSdkFlavor private constructor() : PythonSdkFlavor() {
    override fun isValidSdkHome(path: String): Boolean = File(path).let {
        it.isFile && isValidSdkPath(it) || isMayaFolder(it)
    }

    override fun isValidSdkPath(file: File): Boolean =
        FileUtil.getNameWithoutExtension(file).toLowerCase().startsWith("mayapy")

    override fun getVersionOption(): String = "--version"

    override fun getLanguageLevelFromVersionString(version: String?): LanguageLevel {
        if (version != null && version.startsWith(verStringPrefix)) {
            return LanguageLevel.fromPythonVersion(version.substring(verStringPrefix.length))
                ?: LanguageLevel.getDefault()
        }
        return LanguageLevel.getDefault()
    }

    override fun getLanguageLevel(sdk: Sdk): LanguageLevel = getLanguageLevelFromVersionString(sdk.versionString)

    override fun getLanguageLevel(sdkHome: String): LanguageLevel {
        val version = getVersionString(sdkHome)
        return getLanguageLevelFromVersionString(version)
    }

    override fun getName(): String = "Maya Python"

    override fun getIcon(): Icon = PythonIcons.Python.Python

    override fun getSdkPath(path: VirtualFile): VirtualFile? {
        if (isMayaFolder(File(path.path))) {
            return path.findFileByRelativePath("Contents/bin/mayapy")
        }
        return path
    }

    companion object {
        const val verStringPrefix = "Python "

        val INSTANCE: MayaSdkFlavor = MayaSdkFlavor()

        private fun isMayaFolder(file: File): Boolean = file.isDirectory && file.name == "Maya.app"
    }
}

class MayaFlavorProvider : PythonFlavorProvider {
    override fun getFlavor(platformIndependent: Boolean): PythonSdkFlavor = MayaSdkFlavor.INSTANCE
}
