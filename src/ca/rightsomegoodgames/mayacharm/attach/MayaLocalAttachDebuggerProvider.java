package ca.rightsomegoodgames.mayacharm.attach;

import com.google.common.collect.Lists;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.process.ProcessInfo;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.projectRoots.Sdk;
import com.intellij.openapi.util.Key;
import com.intellij.openapi.util.UserDataHolder;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.containers.ContainerUtil;
import com.intellij.xdebugger.attach.XLocalAttachDebugger;
import com.intellij.xdebugger.attach.XLocalAttachDebuggerProvider;
import com.intellij.xdebugger.attach.XLocalAttachGroup;
import com.jetbrains.python.sdk.PythonSdkType;
import org.jetbrains.annotations.NotNull;
//import ca.rightsomegoodgames.mayacharm.attach.MayaAttachToProcessDebugRunner;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class MayaLocalAttachDebuggerProvider implements XLocalAttachDebuggerProvider {
    private static final Key<List<XLocalAttachDebugger>> DEBUGGERS_KEY = Key.create("MayaLocalAttachDebuggerProvider.DEBUGGERS");
    public List<String> executableNamesList = Lists.newArrayList("mayapy", "maya");

    @NotNull
    @Override
    public XLocalAttachGroup getAttachGroup() {
        return MayaLocalAttachGroup.INSTANCE;
    }

    @NotNull
    @Override
    public List<XLocalAttachDebugger> getAvailableDebuggers(@NotNull Project project,
                                                            @NotNull ProcessInfo processInfo,
                                                            @NotNull UserDataHolder contextHolder) {
        final String executableName = processInfo.getExecutableName();

        for (String possibleExecutableName : executableNamesList) {
            if (StringUtil.containsIgnoreCase(executableName, possibleExecutableName)) {
                List<XLocalAttachDebugger> result = contextHolder.getUserData(DEBUGGERS_KEY);
                if (result != null) return result;

                Optional<String> cannonicalPath = processInfo.getExecutableCannonicalPath();
                if (cannonicalPath.isPresent()) {
                    String pathname = cannonicalPath.get();
                    if (new File(pathname).exists()) {
                        result = Lists.newArrayList(new PyLocalAttachDebugger(pathname));
                    }
                } else {
                    result = ContainerUtil.map(
                            PythonSdkType.getAllLocalCPythons(),
                            PyLocalAttachDebugger::new
                    );
                }

                // most recent python version goes first
                result.sort((a, b) -> -a.getDebuggerDisplayName().compareToIgnoreCase(b.getDebuggerDisplayName()));
                contextHolder.putUserData(DEBUGGERS_KEY, Collections.unmodifiableList(result));
                return result;
            }
        }
        return Collections.emptyList();
    }

    private static class PyLocalAttachDebugger implements XLocalAttachDebugger {
        private final String mySdkHome;
        @NotNull private final String myName;

        public PyLocalAttachDebugger(@NotNull Sdk sdk) {
            mySdkHome = sdk.getHomePath();
            myName = PythonSdkType.getInstance().getVersionString(sdk) + " Debugger";
        }

        public PyLocalAttachDebugger(@NotNull String sdkHome) {
            mySdkHome = sdkHome;
            myName = "Python Debugger";
        }

        @NotNull
        @Override
        public String getDebuggerDisplayName() {
            return myName;
        }

        @Override
        public void attachDebugSession(@NotNull Project project, @NotNull ProcessInfo processInfo) throws ExecutionException {
            MayaAttachToProcessDebugRunner runner = new MayaAttachToProcessDebugRunner(project, processInfo.getPid(), mySdkHome);
            runner.launch();
        }
    }
}
