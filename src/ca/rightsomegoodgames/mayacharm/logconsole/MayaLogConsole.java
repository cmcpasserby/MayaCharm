package ca.rightsomegoodgames.mayacharm.logconsole;

import ca.rightsomegoodgames.mayacharm.resources.PythonStrings;
import com.intellij.diagnostic.logging.LogConsoleImpl;
import com.intellij.diagnostic.logging.LogFragment;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Key;
import com.intellij.psi.search.GlobalSearchScope;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class MayaLogConsole extends LogConsoleImpl {
    public MayaLogConsole(Project project, File file, Charset charset, long l, String s, boolean b, GlobalSearchScope globalSearchScope) {
        super(project, file, charset, l, s, b, globalSearchScope);
        super.setContentPreprocessor(x-> {
            final List<LogFragment> lFrag = new ArrayList<>();
            boolean checks = x.startsWith(PythonStrings.PYSTDERR) || x.startsWith(PythonStrings.PYSTDWRN);
            Key outType = (checks) ? ProcessOutputTypes.STDERR : ProcessOutputTypes.STDOUT;
            lFrag.add(new LogFragment(x, outType));
            return lFrag;
        });
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public synchronized void clear() {
        super.clear();
        final String mayaLogPath = PathManager.getPluginTempPath() + "/mayaLog.txt";
        try {
            PrintWriter writer = new PrintWriter(mayaLogPath);
            writer.print("");
            writer.close();
            System.out.print("Clear Log File");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
