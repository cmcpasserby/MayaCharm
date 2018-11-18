package ca.rightsomegoodgames.mayacharm.ui

import ca.rightsomegoodgames.mayacharm.settings.SdkPortPair
import com.intellij.ui.AddEditRemovePanel
import com.intellij.ui.IdeBorderFactory
import com.intellij.ui.ToolbarDecorator
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout

private class SdkTableModel : AddEditRemovePanel.TableModel<SdkPortPair>() {
    override fun getColumnCount(): Int {
        return 2
    }

    override fun getColumnName(cIndex: Int): String? {
        return if (cIndex == 0) "Maya Version" else "Command Port"
    }

    override fun getField(o: SdkPortPair, cIndex: Int): Any {
        return if (cIndex == 0) o.first else o.second
    }
}

private val model = SdkTableModel()

class SdkTablePanel : AddEditRemovePanel<SdkPortPair>(model, arrayListOf()) {
    override fun addItem(): SdkPortPair? {
        return "" to -1
    }

    override fun removeItem(o: SdkPortPair): Boolean {
        return false
    }

    override fun editItem(o: SdkPortPair): SdkPortPair? {
        // TODO: add a popup dialog here
        return o.first to o.second + 1
    }

    override fun initPanel() {
        layout = BorderLayout()
        val decorator = ToolbarDecorator.createDecorator(table)
        decorator.setEditAction {doEdit()}

        val panel = decorator.createPanel()
        add(panel, BorderLayout.CENTER)
        val label = labelText
        if (label != null) {
            UIUtil.addBorder(panel, IdeBorderFactory.createTitledBorder(label, false))
        }
    }
}
