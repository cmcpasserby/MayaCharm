package ca.rightsomegoodgames.mayacharm.actions

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommandInterface
import ca.rightsomegoodgames.mayacharm.settings.ProjectSettings
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.LangDataKeys

class SendBufferAction : BaseSendAction() {
    override fun actionPerformed(e: AnActionEvent) {
        val settings = ProjectSettings.getInstance(e.project!!)
        val data = e.getData(LangDataKeys.VIRTUAL_FILE)
        if (data != null) {
            val maya = MayaCommandInterface(settings.host, settings.port)
            maya.sendFileToMaya(data.path)
        }
    }

}
