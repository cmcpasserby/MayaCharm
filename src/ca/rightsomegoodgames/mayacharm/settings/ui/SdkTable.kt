package ca.rightsomegoodgames.mayacharm.settings.ui

import ca.rightsomegoodgames.mayacharm.settings.ApplicationSettings
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.AddEditRemovePanel
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.ToolbarDecorator
import com.intellij.util.ui.UIUtil
import com.jetbrains.python.configuration.PyConfigurableInterpreterList
import com.jetbrains.python.sdk.PythonSdkType
import java.awt.BorderLayout

private class SdkTableModel : AddEditRemovePanel.TableModel<ApplicationSettings.SdkInfo>() {
    override fun getColumnCount(): Int {
        return 2
    }

    override fun getColumnName(cIndex: Int): String? {
        return if (cIndex == 0) "Maya Version" else "Command Port"
    }

    override fun getField(o: ApplicationSettings.SdkInfo, cIndex: Int): Any {
        return if (cIndex == 0) o.mayaPyPath else o.port
    }
}

private val model = SdkTableModel()

class SdkTablePanel(private val project: Project) : AddEditRemovePanel<ApplicationSettings.SdkInfo>(model, arrayListOf()) {
    override fun addItem(): ApplicationSettings.SdkInfo? {
//        PythonSdkType.isInvalid()

        val sdkModel = PyConfigurableInterpreterList.getInstance(project).model

        return ApplicationSettings.SdkInfo("", -1)
    }

    override fun removeItem(o: ApplicationSettings.SdkInfo): Boolean {
        return Messages.showDialog("Remove Maya SDK?", "Remove Maya SDK",
                arrayOf("Yes", "No"), 0,
                IconLoader.getIcon("/icons/MayaCharm_Action@2x.png")) == 0
    }

    override fun editItem(o: ApplicationSettings.SdkInfo): ApplicationSettings.SdkInfo? {
        val dialog = SdkEditDialog(project, o)
        dialog.show()
        return dialog.result
    }

    override fun initPanel() {
        layout = BorderLayout()
        val decorator = ToolbarDecorator.createDecorator(table).apply {
            setAddAction {doAdd()}
            setEditAction {doEdit()}
            setRemoveAction {doRemove()}
        }

        val panel = decorator.createPanel()
        add(panel, BorderLayout.CENTER)

        labelText?.apply {
            UIUtil.addBorder(panel, IdeBorderFactory.createTitledBorder(this, false))
        }
    }
}
