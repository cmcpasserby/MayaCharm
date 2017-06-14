package ca.rightsomegoodgames.mayacharm.logconsole;

import ca.rightsomegoodgames.mayacharm.mayacomms.MayaCommInterface;
import ca.rightsomegoodgames.mayacharm.resources.PythonStrings;
import ca.rightsomegoodgames.mayacharm.settings.MCSettingsProvider;
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
    private final MCSettingsProvider settings;

    public MayaLogConsole(Project project, File file, Charset charset, long l, String s, boolean b, GlobalSearchScope globalSearchScope) {
        super(project, file, charset, l, s, b, globalSearchScope);

        settings = MCSettingsProvider.getInstance(project);

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
        final String mayaLogPath = PathManager.getPluginTempPath()
                + String.format(MayaCommInterface.LOG_FILENAME_STRING, settings.getPort());
        try {
            PrintWriter writer = new PrintWriter(mayaLogPath);
            writer.print("");
            writer.close();
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
