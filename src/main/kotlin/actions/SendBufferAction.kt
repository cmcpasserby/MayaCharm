package actions

import MayaBundle as Loc
import mayacomms.MayaCommandInterface
import resources.MayaNotifications
import settings.ProjectSettings
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys
import com.intellij.openapi.fileEditor.FileDocumentManager

class SendBufferAction : BaseSendAction(
        Loc.message("mayacharm.action.SendDocumentText"),
        Loc.message("mayacharm.action.SendDocumentDescription"), null) {
    override fun actionPerformed(e: AnActionEvent) {
        val sdk = ProjectSettings.getInstance(e.project!!).selectedSdk
        if (sdk == null) {
            Notifications.Bus.notify(MayaNotifications.NO_SDK_SELECTED)
            return
        }

        val docManager = FileDocumentManager.getInstance()
        val data = e.getData(LangDataKeys.VIRTUAL_FILE) ?: return
        data.let { docManager.getDocument(it) }?.also { docManager.saveDocument(it) }
        MayaCommandInterface(sdk.port).sendFileToMaya(data.path)
    }
}
