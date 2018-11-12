package ca.rightsomegoodgames.mayacharm.settings

import ca.rightsomegoodgames.mayacharm.mayacomms.mayaPyExcutableName
import com.intellij.openapi.components.*
import com.jetbrains.python.sdk.PythonSdkType
import java.util.*

typealias SdkPortMap = MutableMap<String, Int>
typealias SdkPortPair = Pair<String, Int>

private val portRange = (4434..4534).toSet()

@State(
        name = "MCAppSettings",
        storages = [Storage(value = "mayacharm.settings.xml", roamingType = RoamingType.DISABLED)]
)
class ApplicationSettings : PersistentStateComponent<ApplicationSettings.State> {
    data class State(var mayaSdkMapping: SdkPortMap = mutableMapOf())
    private var myState = State()

    companion object {
        fun getInstance(): ApplicationSettings {
            return ServiceManager.getService(ApplicationSettings::class.java)
        }
    }

    init {
        val mayaSdk = PythonSdkType.getAllLocalCPythons().filter { it.homePath?.endsWith(mayaPyExcutableName) ?: false }
        val homePaths = mayaSdk.map { it.homePath!! }

        for (path in homePaths) {
            myState.mayaSdkMapping[path] = -1
        }

        assignEmptyPorts()
    }

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        val mayaPySdks = PythonSdkType.getAllLocalCPythons().filter { x -> x.homePath?.endsWith(mayaPyExcutableName) ?: false }
        val homePaths = mayaPySdks.map { it.homePath!! }

        myState.mayaSdkMapping.clear()

        for (path in homePaths) {
            if (state.mayaSdkMapping.containsKey(path)) {
                myState.mayaSdkMapping[path] = state.mayaSdkMapping[path]!!
                continue
            }
            myState.mayaSdkMapping[path] = -1
        }

        assignEmptyPorts()
    }

    var mayaSdkMapping: SdkPortMap
        get() = myState.mayaSdkMapping
        set(value) {myState.mayaSdkMapping = value}

    private fun assignEmptyPorts() {
        val usedPorts = myState.mayaSdkMapping.map { it.value }.filter { it > 0 }.toSet()
        val freePorts = PriorityQueue((portRange - usedPorts).sorted())

        for (key in myState.mayaSdkMapping.filter { it.value < 0 }.keys) {
            myState.mayaSdkMapping[key] = freePorts.remove()
        }
    }
}
