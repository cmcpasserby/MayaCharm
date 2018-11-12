package ca.rightsomegoodgames.mayacharm.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.*
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import javax.swing.JComponent

class MayaSdkConfigurable : SearchableConfigurable, Configurable.NoScroll {
    private val settings = ApplicationSettings.getInstance()
    private val myPanel: MayaPySdkTablePanel = MayaPySdkTablePanel()

    override fun getId(): String {
        return ID
    }

    override fun getDisplayName(): String {
        return "MayaCharm MayaSdk"
    }

    override fun getHelpTopic(): String? {
        return null
    }

    override fun createComponent(): JComponent {
        return myPanel
    }

    override fun isModified(): Boolean {
        val orgEntries = settings.mayaSdkMapping.toMap()
        val newEntries = myPanel.data.map{ it.first to it.second }.toMap()
        return newEntries != orgEntries
    }

    override fun reset() {
        myPanel.data.clear()
        myPanel.data.addAll(settings.mayaSdkMapping.entries.map { it.key to it.value })
    }

    override fun apply() {
        settings.mayaSdkMapping = myPanel.data.toMap().toMutableMap()
    }

    companion object {
        public const val ID = "ca.rightsomegoodgames.mayacharm.settings.MayaSdkConfigurable"

        private class MayaPySdkTableModel : AddEditRemovePanel.TableModel<SdkPortPair>() {
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

        private val ourModel = MayaPySdkTableModel()

        private class MayaPySdkTablePanel : AddEditRemovePanel<SdkPortPair>(ourModel, arrayListOf()) {
            override fun addItem(): Pair<String, Int> {
                return "" to -1
            }

            override fun removeItem(o: SdkPortPair): Boolean {
                return false
            }

            override fun editItem(o: SdkPortPair): SdkPortPair {
                return o.first to o.second + 1
            }

            override fun initPanel() {
                layout = BorderLayout()

                val decorator = ToolbarDecorator.createDecorator(table)

                decorator.setEditAction { doEdit() }

                val panel = decorator.createPanel()
                add(panel, BorderLayout.CENTER)
                val label = labelText
                if (label != null) {
                    UIUtil.addBorder(panel, IdeBorderFactory.createTitledBorder(label, false))
                }
            }
        }
    }
}
