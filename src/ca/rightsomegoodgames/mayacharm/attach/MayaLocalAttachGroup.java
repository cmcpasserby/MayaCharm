package ca.rightsomegoodgames.mayacharm.attach;

import com.intellij.execution.process.ProcessInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.IconLoader;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.xdebugger.attach.XLocalAttachGroup;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class MayaLocalAttachGroup implements XLocalAttachGroup {
    public static final MayaLocalAttachGroup INSTANCE = new MayaLocalAttachGroup();

    private MayaLocalAttachGroup() {
    }

    @Override
    public int getOrder() {
        return XLocalAttachGroup.DEFAULT.getOrder() - 10;
    }

    @NotNull
    @Override
    public String getGroupName() {
        return "Maya(py)";
    }

    @NotNull
    @Override
    public Icon getProcessIcon(@NotNull Project project, @NotNull ProcessInfo info, @NotNull UserDataHolder dataHolder) {
        return IconLoader.getIcon("/icons/MayaCharm_Action.png", MayaLocalAttachGroup.class);
    }

    @NotNull
    @Override
    public String getProcessDisplayText(@NotNull Project project, @NotNull ProcessInfo info, @NotNull UserDataHolder dataHolder) {
        return info.getArgs();
    }

    @Override
    public int compare(@NotNull Project project, @NotNull ProcessInfo a, @NotNull ProcessInfo b, @NotNull UserDataHolder dataHolder) {
        return XLocalAttachGroup.DEFAULT.compare(project, a, b, dataHolder);
    }
}
