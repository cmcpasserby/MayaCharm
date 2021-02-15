package actions

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.fileTypes.FileType
import com.intellij.openapi.fileTypes.FileTypeManager
import com.intellij.openapi.project.DumbAwareAction
import javax.swing.Icon

abstract class BaseSendAction(text: String, description: String, icon: Icon?) :
    DumbAwareAction(text, description, icon) {
    companion object {
        private val fileTypeManager: FileTypeManager = FileTypeManager.getInstance()
        private val pyFileType: FileType = fileTypeManager.findFileTypeByName("Python")!!
    }

    abstract override fun actionPerformed(e: AnActionEvent)

    override fun update(e: AnActionEvent) {
        val currentFile = e.getData(LangDataKeys.VIRTUAL_FILE)
        if (currentFile == null) {
            e.presentation.isEnabled = false
            return
        }
        e.presentation.isEnabled = fileTypeManager.isFileOfType(currentFile, pyFileType)
    }
}
