package ca.rightsomegoodgames.mayacharm.actions

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.resources.MayaNotifications
import ca.rightsomegoodgames.mayacharm.settings.ProjectSettings
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.AnActionEvent

class ConnectToMayaLogAction : BaseSendAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val sdk = ProjectSettings.getInstance(e.project!!).selectedSdk

        if (sdk == null) {
            Notifications.Bus.notify(MayaNotifications.NO_SDK_SELECTED)
            return
        }

        val maya = MayaCommandInterface(sdk.port)
        maya.connectMayaLog()
    }
}
