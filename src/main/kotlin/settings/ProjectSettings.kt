package settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.getProjectCachePath
import com.intellij.util.io.createDirectories
import com.intellij.util.io.delete
import java.io.File
import java.nio.file.Path
import java.nio.file.Paths

@State(
    name = "MCProjectSettings",
    storages = [Storage(value = StoragePathMacros.WORKSPACE_FILE)]
)
class ProjectSettings(val project: Project) : PersistentStateComponent<ProjectSettings.State> {
    private val appSettings = ApplicationSettings.INSTANCE

    data class State(var selectedSdk: String? = null)

    private var myState = State()

    val pythonCachePath: Path = Paths.get(project.getProjectCachePath("MayaCharm").toString(), "python")

    init {
        unpackResources()
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState.selectedSdk = state.selectedSdk
    }

    var selectedSdkName: String?
        get() = myState.selectedSdk ?: appSettings.mayaSdkMapping.keys.maxOrNull()
        set(value) {
            myState.selectedSdk = value
        }

    val selectedSdk: ApplicationSettings.SdkInfo?
        get() = appSettings.mayaSdkMapping[selectedSdkName]

    private fun unpackResources() {
        val pythonCache = pythonCachePath
        pythonCache.delete(true)
        pythonCache.createDirectories()

        for (resPath in listOf("attach_pydevd.py", "attach_script.py")) {
            val res = this::class.java.classLoader.getResource("python/$resPath") ?: continue
            File("$pythonCache/$resPath").writeBytes(res.readBytes())
        }
    }

    companion object {
        fun getInstance(project: Project): ProjectSettings {
            return project.service()
        }
    }
}
