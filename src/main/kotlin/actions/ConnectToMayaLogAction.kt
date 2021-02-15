package actions

import MayaBundle as Loc
import mayacomms.MayaCommandInterface
import resources.MayaNotifications
import settings.ProjectSettings
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent

class ConnectToMayaLogAction : BaseSendAction(
    Loc.message("mayacharm.action.ConnectLogText"),
    Loc.message("mayacharm.action.ConnectLogDescription"), null
) {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val sdk = ProjectSettings.getInstance(e.project!!).selectedSdk

        if (sdk == null) {
            Notifications.Bus.notify(MayaNotifications.NO_SDK_SELECTED)
            return
        }

        MayaCommandInterface(sdk.port).connectMayaLog()
    }
}
