package ca.rightsomegoodgames.mayacharm.actions

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.settings.SettingsProvider
import com.intellij.openapi.actionSystem.AnActionEvent

class ConnectToMayaLogAction : BaseSendAction() {
    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = true
    }

    override fun actionPerformed(e: AnActionEvent) {
        val settings = SettingsProvider.getInstance(e.project!!)
        val maya = MayaCommandInterface(settings.host, settings.port)
        maya.connectMayaLog()
    }
}
