package ca.rightsomegoodgames.mayacharm.ui

import com.intellij.openapi.ui.ComboBox
import java.awt.GridBagConstraints
import java.awt.GridBagLayout
import java.awt.Insets
import javax.swing.JLabel
import javax.swing.JPanel

class SdkSelector : JPanel(GridBagLayout()) {
    private val comboBox = ComboBox<String>()

    init {
        val c = GridBagConstraints()
        c.insets = Insets(2, 2, 2, 2)
        c.gridx = 0
        c.gridy = 0
        c.fill = GridBagConstraints.HORIZONTAL
        add(JLabel("Active Maya SDK: "), c)

        c.gridx = 1
        c.weightx = 0.1
        add(comboBox, c)
    }

    var selectedItem: String?
        get() = comboBox.selectedItem as String?
        set(value) {comboBox.selectedItem = value}

    var items: List<String>
        get() = List<String>(comboBox.itemCount) {comboBox.getItemAt(it)}
        set(value) {
            comboBox.removeAllItems()
            for (s in value) {
                comboBox.addItem(s)
            }
        }

}
