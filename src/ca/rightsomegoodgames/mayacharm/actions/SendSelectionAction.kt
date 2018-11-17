package ca.rightsomegoodgames.mayacharm.actions

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.settings.ProjectSettings
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys

class SendSelectionAction : BaseSendAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val settings = ProjectSettings.getInstance(e.project!!)
        val editor = e.getData(LangDataKeys.EDITOR) ?: return

        val selectionModel = editor.selectionModel
        val selectedText: String?

        if (selectionModel.hasSelection()) {
            selectedText = selectionModel.selectedText
        }
        else {
            selectionModel.selectLineAtCaret()
            if (selectionModel.hasSelection()) {
                selectedText = selectionModel.selectedText
                selectionModel.removeSelection()
            }
            else {
                return
            }
        }

        val maya = MayaCommandInterface(settings.host, settings.port!!)
        if (selectedText != null) {
            maya.sendCodeToMaya(selectedText)
        }
    }
}
