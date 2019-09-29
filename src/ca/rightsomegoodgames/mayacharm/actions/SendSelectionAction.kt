package ca.rightsomegoodgames.mayacharm.actions

import ca.rightsomegoodgames.mayacharm.MayaBundle as Loc
import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.resources.MayaNotifications
import ca.rightsomegoodgames.mayacharm.settings.ProjectSettings
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys

class SendSelectionAction: BaseSendAction(
        Loc.message("mayacharm.action.SendSelectionText"),
        Loc.message("mayacharm.action.SendSelectionDescription"), null) {
    override fun actionPerformed(e: AnActionEvent) {
        val sdk = ProjectSettings.getInstance(e.project!!).selectedSdk
        if (sdk == null) {
            Notifications.Bus.notify(MayaNotifications.NO_SDK_SELECTED)
            return
        }

        val editor = e.getData(LangDataKeys.EDITOR)!!

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
            } else return
        }

        val maya = MayaCommandInterface(sdk.port)
        if (selectedText != null) {
            maya.sendCodeToMaya(selectedText)
        }
    }
}
