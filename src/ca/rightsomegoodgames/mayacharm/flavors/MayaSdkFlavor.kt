package ca.rightsomegoodgames.mayacharm.flavors

import ca.rightsomegoodgames.mayacharm.MayaBundle as Loc
import com.intellij.openapi.fileChooser.FileChooserDescriptor
import com.intellij.openapi.util.SystemInfo
import com.intellij.openapi.util.io.FileUtil
import com.intellij.openapi.vfs.VirtualFile
import com.jetbrains.python.PyBundle
import com.jetbrains.python.sdk.flavors.PythonFlavorProvider
import com.jetbrains.python.sdk.flavors.PythonSdkFlavor
import icons.PythonIcons
import java.io.File
import java.lang.Exception
import javax.swing.Icon

class MayaSdkFlavor private constructor() : PythonSdkFlavor() {
    override fun isValidSdkHome(path: String): Boolean {
        val file = File(path)
        return file.isFile && isValidSdkPath(file) || isMayaFolder(file)
    }

    override fun isValidSdkPath(file: File): Boolean {
        val name = FileUtil.getNameWithoutExtension(file).toLowerCase()
        return name.startsWith("mayapy")
    }

    override fun getVersionOption(): String {
        return "--version"
    }

    override fun getName(): String {
        return "MayaPy"
    }

    override fun getIcon(): Icon {
        return PythonIcons.Python.Python
    }

    override fun getSdkPath(path: VirtualFile): VirtualFile? {
        if (isMayaFolder(File(path.path))) {
            return path.findFileByRelativePath("Contents/bin/mayapy")
        }
        return path
    }

    public fun getHomeChooserDescriptor(): FileChooserDescriptor {
        val isWindows = SystemInfo.isWindows

        return object : FileChooserDescriptor(true, false, false, false, false, false) {
            override fun validateSelectedFiles(files: Array<out VirtualFile>) {
                if (files.count() != 0) {
                    if (!isValidSdkHome(files[0].path)) {
                        throw Exception(Loc.message("mayacharm.exceptions.InvalidMayaPy", files[0].name))
                    }
                }
            }

            override fun isFileVisible(file: VirtualFile?, showHiddenFiles: Boolean): Boolean {
                if (file == null) {
                    return false
                }

                if (!file.isDirectory) {
                    if (isWindows) {
                        val path = file.path
                        return path.endsWith("exe") && super.isFileVisible(file, showHiddenFiles)
                    }
                }
                return super.isFileVisible(file, showHiddenFiles)
            }
        }.withTitle(PyBundle.message("sdk.select.path")).withShowHiddenFiles(SystemInfo.isUnix)
    }

    companion object {
        val INSTANCE: MayaSdkFlavor = MayaSdkFlavor()

        private fun isMayaFolder(file: File) : Boolean {
            return file.isDirectory && file.name == "Maya.app"
        }
    }
}

class MayaFlavorProvider : PythonFlavorProvider {
    override fun getFlavor(platformIndependent: Boolean): PythonSdkFlavor? = MayaSdkFlavor.INSTANCE
}
