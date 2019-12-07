package settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
        name = "MCProjectSettings",
        storages = [Storage(value = StoragePathMacros.WORKSPACE_FILE)]
)
class ProjectSettings : PersistentStateComponent<ProjectSettings.State> {
    private val appSettings = ApplicationSettings.INSTANCE

    data class State(var selectedSdk: String? = null)
    private var myState = State()

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState.selectedSdk = state.selectedSdk
    }

    public var selectedSdkName: String?
        get() = myState.selectedSdk ?: appSettings.mayaSdkMapping.keys.sortedDescending().firstOrNull()
        set(value) {myState.selectedSdk = value}

    public val selectedSdk: ApplicationSettings.SdkInfo?
        get() = appSettings.mayaSdkMapping[selectedSdkName]

    companion object {
        public fun getInstance(project: Project): ProjectSettings {
            return ServiceManager.getService(project, ProjectSettings::class.java)
        }
    }
}
