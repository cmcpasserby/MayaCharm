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
        Key outType = (s.contains(PythonStrings.PYSTDERR) || s.contains(PythonStrings.MELSTDERR)) ?
                ProcessOutputTypes.STDERR : ProcessOutputTypes.STDOUT;
        lFrag.add(new LogFragment(s, outType));
        return lFrag;
    }
}
