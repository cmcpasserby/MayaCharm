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
import settings.ApplicationSettings
import settings.ProjectSettings
import java.nio.file.Paths

class MayaAttachToProcessCliState(runConfig: PythonRunConfiguration, env: ExecutionEnvironment) :
    PythonScriptCommandLineState(runConfig, env) {
    companion object {
        fun create(project: Project, sdkPath: String, port: Int, pid: Int, mayaSdk: ApplicationSettings.SdkInfo): MayaAttachToProcessCliState {
            val conf =
                PythonConfigurationType.getInstance().factory.createTemplateConfiguration(project) as PythonRunConfiguration
            val env = ExecutionEnvironmentBuilder.create(project, DefaultDebugExecutor.getDebugExecutorInstance(), conf)
                .build()
            val projectSettings = ProjectSettings.getInstance(project)

            val mcPort = mayaSdk.port
            val debuggerPath = PythonHelper.DEBUGGER.pythonPathEntry

            PythonEnvUtil.addToPythonPath(conf.envs, listOf(env.project.basePath))

            conf.workingDirectory = env.project.basePath
            conf.sdkHome = sdkPath
            conf.isUseModuleSdk = false
            conf.scriptName = Paths.get(projectSettings.pythonCachePath.toString(), "attach_pydevd.py").toString()
            conf.scriptParameters = "--port $port --pid $pid --mcPort $mcPort --pydevPath \"$debuggerPath\""

            return MayaAttachToProcessCliState(conf, env)
        }
    }

    override fun doCreateProcess(commandLine: GeneralCommandLine?): ProcessHandler {
        val handler = super.doCreateProcess(commandLine)
        return PyAttachToProcessCommandLineState.PyRemoteDebugProcessHandler(handler)
    }
}

