package ca.rightsomegoodgames.mayacharm.actions

import ca.rightsomegoodgames.mayacharm.MayaBundle as Loc
import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.resources.MayaNotifications
import ca.rightsomegoodgames.mayacharm.settings.ProjectSettings
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
        val data = e.getData(LangDataKeys.VIRTUAL_FILE)

        val doc = data?.let { docManager.getDocument(it) }
        doc?.let {docManager.saveDocument(it)}

        if (data != null) {
            val maya = MayaCommandInterface(sdk.port)
            maya.sendFileToMaya(data.path)
        }
    }
}
