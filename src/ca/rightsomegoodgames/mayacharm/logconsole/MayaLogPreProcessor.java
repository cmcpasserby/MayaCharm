package ca.rightsomegoodgames.mayacharm.logconsole;

import ca.rightsomegoodgames.mayacharm.resources.PythonStrings;
import com.intellij.diagnostic.logging.LogContentPreprocessor;
import com.intellij.diagnostic.logging.LogFragment;
import com.intellij.execution.process.ProcessOutputTypes;
import com.intellij.openapi.util.Key;

import java.util.ArrayList;
import java.util.List;

public class MayaLogPreProcessor implements LogContentPreprocessor {
    @Override
    public List<LogFragment> parseLogLine(String s) {
        final List<LogFragment> lFrag = new ArrayList<>();
        boolean checks = s.startsWith(PythonStrings.PYSTDERR) || s.startsWith(PythonStrings.MELSTDERR) ||
                s.startsWith(PythonStrings.MELSRDWRN) || s.startsWith(PythonStrings.PYSTDWRN);
        Key outType = (checks) ? ProcessOutputTypes.STDERR : ProcessOutputTypes.STDOUT;
        lFrag.add(new LogFragment(s, outType));
        return lFrag;
    }
}
