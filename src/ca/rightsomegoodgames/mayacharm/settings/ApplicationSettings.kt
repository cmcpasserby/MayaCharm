package ca.rightsomegoodgames.mayacharm.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project

@State(
        name = "MCAppSettings",
        storages = [Storage(value = "MayaCharmSettings.xml", roamingType = RoamingType.DISABLED)]
)
class ApplicationSettings : PersistentStateComponent<ApplicationSettings.State> {
    data class State(var mayaPath: String = "", var mayaPyPath: String = "")
    private var myState = State()

    companion object {
        fun getInstance(project: Project): ApplicationSettings {
            return ServiceManager.getService(project, ApplicationSettings::class.java)
        }
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState.mayaPath = state.mayaPath
        myState.mayaPyPath = state.mayaPyPath
    }

    public var mayaPath: String
        get() = myState.mayaPath
        set(value) {myState.mayaPath = value}

    public var mayaPyPath: String
        get() = myState.mayaPyPath
        set(value) {myState.mayaPyPath = value}
}
