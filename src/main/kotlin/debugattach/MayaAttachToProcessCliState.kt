package debugattach

import com.intellij.execution.configurations.GeneralCommandLine
import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.process.ProcessHandler
import com.intellij.execution.runners.ExecutionEnvironment
import com.intellij.execution.runners.ExecutionEnvironmentBuilder
import com.intellij.openapi.project.Project
import com.jetbrains.python.PythonHelper
import com.jetbrains.python.debugger.attach.PyAttachToProcessCommandLineState
import com.jetbrains.python.run.PythonConfigurationType
import com.jetbrains.python.run.PythonRunConfiguration
import com.jetbrains.python.run.PythonScriptCommandLineState
import com.jetbrains.python.sdk.PythonEnvUtil

class MayaAttachToProcessCliState(runConfig: PythonRunConfiguration, env: ExecutionEnvironment) : PythonScriptCommandLineState(runConfig, env) {
    companion object {
        fun create(project: Project, sdkPath: String, port: Int, pid: Int) : MayaAttachToProcessCliState {
            val conf = PythonConfigurationType.getInstance().factory.createTemplateConfiguration(project) as PythonRunConfiguration
            val env = ExecutionEnvironmentBuilder.create(project, DefaultDebugExecutor.getDebugExecutorInstance(), conf).build()

            PythonEnvUtil.addToPythonPath(conf.envs, listOf(env.project.basePath))

            conf.workingDirectory = env.project.basePath
            conf.sdkHome = sdkPath
            conf.isUseModuleSdk = false
            conf.scriptName = PythonHelper.ATTACH_DEBUGGER.asParamString()
            conf.scriptParameters = "--port $port --pid $pid"

            return MayaAttachToProcessCliState(conf, env)
        }
    }

    override fun doCreateProcess(commandLine: GeneralCommandLine?): ProcessHandler {
        val handler = super.doCreateProcess(commandLine)
        return PyAttachToProcessCommandLineState.PyRemoteDebugProcessHandler(handler)
    }
}

