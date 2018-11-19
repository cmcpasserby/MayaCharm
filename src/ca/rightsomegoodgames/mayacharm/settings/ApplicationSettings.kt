package ca.rightsomegoodgames.mayacharm.settings

import ca.rightsomegoodgames.mayacharm.mayacomms.mayaFromMayaPy
import ca.rightsomegoodgames.mayacharm.mayacomms.mayaPyExecutableName
import com.intellij.openapi.components.*
import com.jetbrains.python.sdk.PythonSdkType
import java.util.*

typealias SdkPortMap = MutableMap<String, ApplicationSettings.SdkInfo>

private val portRange = (4434..4534).toSet()

@State(
        name = "MCAppSettings",
        storages = [Storage(value = "mayacharm.settings.xml", roamingType = RoamingType.DISABLED)]
)
class ApplicationSettings : PersistentStateComponent<ApplicationSettings.State> {
    data class SdkInfo(var mayaPyPath: String = "", var port: Int = -1) {
        val mayaPath: String
            get() = mayaFromMayaPy(mayaPyPath) ?: ""
    }

    data class State(var mayaSdkMapping: SdkPortMap = mutableMapOf())
    private var myState = State()

    companion object {
        fun getInstance(): ApplicationSettings {
            return ServiceManager.getService(ApplicationSettings::class.java)
        }
    }

    init {
        val mayaSdk = PythonSdkType.getAllLocalCPythons().filter { it.homePath?.endsWith(mayaPyExecutableName) ?: false }
        val homePaths = mayaSdk.map { it.homePath!! }

        for (path in homePaths) {
            myState.mayaSdkMapping[path] = SdkInfo(path, -1)
        }
        assignEmptyPorts()
    }

    var mayaSdkMapping: SdkPortMap
        get() = myState.mayaSdkMapping
        set(value) {myState.mayaSdkMapping = value}

    override fun getState(): State {
        return myState
    }

    override fun loadState(state: State) {
        val mayaPySdks = PythonSdkType.getAllLocalCPythons().filter { x -> x.homePath?.endsWith(mayaPyExecutableName) ?: false }
        val homePaths = mayaPySdks.map { it.homePath!! }

        myState.mayaSdkMapping.clear()

        for (path in homePaths) {
            if (state.mayaSdkMapping.containsKey(path)) {
                myState.mayaSdkMapping[path] = state.mayaSdkMapping[path]!!
                continue
            }
            myState.mayaSdkMapping[path] = SdkInfo(path, -1)
        }
        assignEmptyPorts()
    }

    private fun assignEmptyPorts() {
        val usedPorts = myState.mayaSdkMapping.map { it.value.port }.filter { it > 0 }.toSet()
        val freePorts = PriorityQueue((portRange - usedPorts).sorted())

        for (key in myState.mayaSdkMapping.filter { it.value.port < 0 }.keys) {
            myState.mayaSdkMapping[key]!!.port = freePorts.remove()
        }
    }
}
