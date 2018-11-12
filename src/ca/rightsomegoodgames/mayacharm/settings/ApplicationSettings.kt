package ca.rightsomegoodgames.mayacharm.settings

import com.intellij.openapi.components.*

@State(
        name = "MCAppSettings",
        storages = [Storage(value = "mayacharm.settings.xml", roamingType = RoamingType.DISABLED)]
)
class ApplicationSettings : PersistentStateComponent<ApplicationSettings.State> {
    data class State(var mayaSdkMapping: Map<String, Int> = mapOf("Maya 2016" to 9000, "Maya 2017" to 9001))

    private var myState = State()

    companion object {
        fun getInstance(): ApplicationSettings {
            return ServiceManager.getService(ApplicationSettings::class.java)
        }
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        myState.mayaSdkMapping = state.mayaSdkMapping
    }

    var mayaSdkMapping: Map<String, Int>
        get() = myState.mayaSdkMapping
        set(value) {myState.mayaSdkMapping = value}
}
