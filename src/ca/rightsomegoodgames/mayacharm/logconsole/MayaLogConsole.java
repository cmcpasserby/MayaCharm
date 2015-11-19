package ca.rightsomegoodgames.mayacharm.logconsole;

import com.intellij.diagnostic.logging.LogConsoleImpl;
import com.intellij.openapi.application.PathManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.search.GlobalSearchScope;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.charset.Charset;

public class MayaLogConsole extends LogConsoleImpl {
    public MayaLogConsole(Project project, File file, Charset charset, long l, String s, boolean b, GlobalSearchScope globalSearchScope) {
        super(project, file, charset, l, s, b, globalSearchScope);
        super.setContentPreprocessor(new MayaLogPreProcessor());
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
            new PrintWriter(mayaLogPath).close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
