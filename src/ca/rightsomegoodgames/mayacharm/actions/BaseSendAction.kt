package ca.rightsomegoodgames.mayacharm.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.fileTypes.FileTypeManager

abstract class BaseSendAction : AnAction() {
    private val fileTypeManager: FileTypeManager = FileTypeManager.getInstance()

    abstract override fun actionPerformed(e: AnActionEvent)

    override fun update(e: AnActionEvent) {
        val currentFile = e.getData(LangDataKeys.VIRTUAL_FILE)
        if (currentFile == null) {
            e.presentation.isEnabled = false
            return
        }

        e.presentation.isEnabled = fileTypeManager.isFileOfType(currentFile, fileTypeManager.getFileTypeByExtension(".py"))
    }
}
