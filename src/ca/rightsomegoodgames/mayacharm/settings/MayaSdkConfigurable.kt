package ca.rightsomegoodgames.mayacharm.settings

import com.google.common.collect.ImmutableMap
import com.google.common.collect.Maps
import com.intellij.icons.AllIcons
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.ui.*
import com.intellij.util.ui.UIUtil
import java.awt.BorderLayout
import java.awt.Toolkit
import javax.swing.JButton
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
        val newEntries = myPanel.data.map { it.key to it.value }.toMap()
        return newEntries != orgEntries
    }

    override fun reset() {
        myPanel.data.clear()
        myPanel.data.addAll(settings.mayaSdkMapping.entries.toSet())
    }

    override fun apply() {
        settings.mayaSdkMapping = myPanel.data.map { it.key to it.value }.toMap()
    }

    companion object {
        public const val ID = "ca.rightsomegoodgames.mayacharm.settings.MayaSdkConfigurable"

        private class MayaPySdkTableModel : AddEditRemovePanel.TableModel<Map.Entry<String, Int>>() {
            override fun getColumnCount(): Int {
                return 2
            }

            override fun getColumnName(cIndex: Int): String? {
                return if (cIndex == 0) "Maya Version" else "Command Port"
            }

            override fun getField(o: Map.Entry<String, Int>, cIndex: Int): Any {
                return if (cIndex == 0) o.key else o.value
            }
        }

        private val ourModel = MayaPySdkTableModel()

        private class MayaPySdkTablePanel : AddEditRemovePanel<Map.Entry<String, Int>>(ourModel, arrayListOf()) {
            override fun addItem(): Map.Entry<String, Int> {
                return Maps.immutableEntry("Maya 2016", 4096)
            }

            override fun removeItem(o: Map.Entry<String, Int>): Boolean {
                return false
            }

            override fun editItem(o: Map.Entry<String, Int>): Map.Entry<String, Int> {
                return Maps.immutableEntry("Maya 2016", o.value + 1)
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
