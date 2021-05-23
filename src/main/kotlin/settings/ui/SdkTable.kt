package settings.ui

import MayaBundle as Loc
import settings.ApplicationSettings
import utils.Delegate
import utils.Event
import com.intellij.openapi.project.Project
import com.intellij.openapi.projectRoots.impl.SdkConfigurationUtil
import com.intellij.openapi.ui.Messages
import com.intellij.openapi.util.IconLoader
import com.intellij.ui.AddEditRemovePanel
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.ToolbarDecorator
import com.intellij.util.ui.UIUtil
import com.jetbrains.python.sdk.PythonSdkUtil
import java.awt.BorderLayout

private class SdkTableModel : AddEditRemovePanel.TableModel<ApplicationSettings.SdkInfo>() {
    override fun getColumnCount(): Int = 3

    override fun getColumnName(cIndex: Int): String = when (cIndex) {
        0 -> "Maya Version"
        1 -> "MayaPy2"
        2 -> "MayaPy3"
        else -> throw IndexOutOfBoundsException()
    }

    override fun getField(o: ApplicationSettings.SdkInfo, cIndex: Int): Any = when (cIndex) {
        0 -> o.mayaPath
        1 -> o.mayaPy2Path
        2 -> o.mayaPy3Path
        else -> throw IndexOutOfBoundsException()
    }
}

private val model = SdkTableModel()

class SdkTablePanel(private val project: Project) :
    AddEditRemovePanel<ApplicationSettings.SdkInfo>(model, arrayListOf()) {
    private val onChanged = Delegate<SdkTablePanel>()
    val changed: Event<SdkTablePanel> get() = onChanged

    override fun addItem(): ApplicationSettings.SdkInfo? {
        val dialog = SdkAddDialog(project)
        dialog.show()

        val result = dialog.result ?: return null
        // TODO check if path from dialog is valid and add to path if ok was pressed

        return null
    }

    override fun removeItem(sdkInfo: ApplicationSettings.SdkInfo): Boolean {
        val result = Messages.showDialog(
            Loc.message("mayacharm.sdkremove.RemoveWarning"), Loc.message("mayacharm.sdkremove.Title"),
            arrayOf(Loc.message("mayacharm.Yes"), Loc.message("mayacharm.No")), 0,
            IconLoader.getIcon("/icons/MayaCharm_Action@2x.png", this::class.java)
        ) == 0

        if (result) {
            PythonSdkUtil.findSdkByPath(sdkInfo.mayaPy2Path)?.let { SdkConfigurationUtil.removeSdk(it) }
            PythonSdkUtil.findSdkByPath(sdkInfo.mayaPy3Path)?.let { SdkConfigurationUtil.removeSdk(it) }
        }
        onChanged(this)
        return result
    }

    override fun editItem(o: ApplicationSettings.SdkInfo): ApplicationSettings.SdkInfo {
        val dialog = SdkEditDialog(project, o)
        dialog.show()
        onChanged(this)
        return dialog.result
    }

    override fun initPanel() {
        layout = BorderLayout()
        val decorator = ToolbarDecorator.createDecorator(table).apply {
            setAddAction { doAdd() }
            setEditAction { doEdit() }
            setRemoveAction { doRemove() }
        }

        val panel = decorator.createPanel()
        add(panel, BorderLayout.CENTER)

        labelText?.apply {
            UIUtil.addBorder(panel, IdeBorderFactory.createTitledBorder(this, false))
        }
    }
}
