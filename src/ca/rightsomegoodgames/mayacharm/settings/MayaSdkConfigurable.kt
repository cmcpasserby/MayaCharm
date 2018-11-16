package ca.rightsomegoodgames.mayacharm.settings

import com.intellij.openapi.options.Configurable
import com.intellij.openapi.options.SearchableConfigurable
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.*
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.UIUtil
import java.awt.*
import javax.swing.JComponent
import javax.swing.JPanel

class MayaSdkConfigurable : SearchableConfigurable, Configurable.NoScroll {
    private val settings = ApplicationSettings.getInstance()

    private val myPanel = JPanel(GridBagLayout())
    private val mySdkSelector = ComboBox<String>()
    private val mySdkPanel = MayaPySdkTablePanel()

    init {
        layout()
    }

    private fun layout() {
        val c = GridBagConstraints()

        val sdkLabel = JBLabel("Active Maya SDK: ")
        c.insets = Insets(2, 2, 2, 2)
        c.gridx = 0
        c.gridy = 0
        c.fill = GridBagConstraints.HORIZONTAL
        myPanel.add(sdkLabel, c)

        c.gridx = 1
        c.gridy = 0
        c.weightx = 0.1
        myPanel.add(mySdkSelector, c)

        c.insets = Insets(2, 2, 0, 2)
        c.gridx = 0
        c.gridy++
        c.weighty = 1.0
        c.gridwidth = 3
        c.gridheight = GridBagConstraints.RELATIVE
        c.fill = GridBagConstraints.BOTH
        myPanel.add(mySdkPanel, c)
    }

    override fun getId(): String {
        return ID
    }

    override fun getDisplayName(): String {
        return "MayaCharm"
    }

    override fun getHelpTopic(): String? {
        return null
    }

    override fun createComponent(): JComponent {
        return myPanel
    }

    override fun isModified(): Boolean {
        val orgEntries = settings.mayaSdkMapping.toMap()
        val newEntries = mySdkPanel.data.map{ it.first to it.second }.toMap()
        return newEntries != orgEntries
    }

    override fun reset() {
        mySdkPanel.data.clear()
        mySdkPanel.data.addAll(settings.mayaSdkMapping.entries.map { it.key to it.value })

        for (entry in settings.mayaSdkMapping) {
            mySdkSelector.addItem(entry.key)
        }
    }

    override fun apply() {
        settings.mayaSdkMapping = mySdkPanel.data.toMap().toMutableMap()
    }

    companion object {
        public const val ID = "ca.rightsomegoodgames.mayacharm.settings.MayaSdkConfigurable"
        private val ourModel = MayaPySdkTableModel()

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
