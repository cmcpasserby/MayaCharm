package ca.rightsomegoodgames.mayacharm.run;

import ca.rightsomegoodgames.mayacharm.run.debug.MayaCharmDebugConfig;

public interface MayaCharmRunProfile {
    MayaCharmDebugConfig.ExecutionType getExecutionType();
    String getScriptFilePath();
    String getScriptCodeText();
}
