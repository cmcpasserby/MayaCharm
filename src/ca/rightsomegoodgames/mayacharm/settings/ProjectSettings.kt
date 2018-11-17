package ca.rightsomegoodgames.mayacharm.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
        name = "MCProjectSettings",
        storages = [Storage(value = StoragePathMacros.WORKSPACE_FILE)]
)
class ProjectSettings : PersistentStateComponent<ProjectSettings.State> {
    private val appSettings = ApplicationSettings.getInstance()

    data class State(var selectedSdk: String? = null)
    private var myState = State()

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState.selectedSdk = state.selectedSdk
    }

    public var selectedSdk: String?
        get() = myState.selectedSdk
        set(value) {myState.selectedSdk = value}

    public val host: String
        get() = "localhost"

    public val port: Int? // TODO find a way to deal with null in the case of a SDK not being selected
        get() = appSettings.mayaSdkMapping[selectedSdk]

    companion object {
        public fun getInstance(project: Project):ProjectSettings {
            return ServiceManager.getService(project, ProjectSettings::class.java)
        }
    }
}
