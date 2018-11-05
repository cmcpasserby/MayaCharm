package ca.rightsomegoodgames.mayacharm.run

import com.intellij.execution.configurations.RunProfile
import com.intellij.execution.configurations.RunProfileState
import com.intellij.execution.configurations.RunnerSettings
import com.intellij.execution.process.impl.ProcessListUtil
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.GenericProgramRunner
import com.intellij.execution.ui.RunContentDescriptor
import com.jetbrains.python.debugger.attach.PyAttachToProcessDebugRunner
import com.jetbrains.python.sdk.PythonSdkType

class MayaCharmDebugRunner : GenericProgramRunner<RunnerSettings>() {
    override fun getRunnerId(): String {
        return "MayaCharmDebugRunner"
    }

    override fun canRun(s: String, runProfile: RunProfile): Boolean {
        return runProfile is MayaCharmRunConfiguration && s == "Debug"
    }

    override fun doExecute(state: RunProfileState, environment: ExecutionEnvironment): RunContentDescriptor? {
        val result = PythonSdkType.getAllLocalCPythons().first { it.homePath!!.contains("mayapy.exe") }
        val process = ProcessListUtil.getProcessList().first { it.executableName == "maya.exe" }

        // TODO: Setup a proper XDebugSession with process starter

        val attachRunner = PyAttachToProcessDebugRunner(environment.project, process.pid, result.homePath)
        attachRunner.launch()

        return null
    }
}
