package ca.rightsomegoodgames.mayacharm.actions;

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommInterface;
import ca.rightsomegoodgames.mayacharm.settings.MCSettingsProvider;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.LangDataKeys;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.editor.SelectionModel;
import org.jetbrains.annotations.NotNull;

public class SendSelectionAction extends BaseSendAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        final MCSettingsProvider settings = MCSettingsProvider.getInstance(e.getProject());
        final Editor editor = e.getData(LangDataKeys.EDITOR);
        if (editor == null)
            return;

        final SelectionModel selectionModel = editor.getSelectionModel();
        String selectedText;

        if (selectionModel.hasSelection()) {
            selectedText = selectionModel.getSelectedText();
        }
        else {
            selectionModel.selectLineAtCaret();
            if (selectionModel.hasSelection()) {
                selectedText = selectionModel.getSelectedText();
                selectionModel.removeSelection();
            }
            else { return; }
        }

        MayaCommInterface maya = new MayaCommInterface(settings.getHost(), settings.getPort());
        maya.connectMayaLog();
        maya.sendCodeToMaya(selectedText);
    }
}
