package settings

import mayacomms.mayaFromMayaPy
import mayacomms.mayaPyExecutableName

import com.intellij.openapi.components.*
import com.jetbrains.python.sdk.PythonSdkUtil
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
        val INSTANCE: ApplicationSettings
            get() = service()
    }

    init {
        val mayaSdk = PythonSdkUtil.getAllLocalCPythons().filter { it.homePath?.endsWith(mayaPyExecutableName) ?: false }
        val homePaths = mayaSdk.map { it.homePath!! }

        for (path in homePaths) {
            mayaSdkMapping[path] = SdkInfo(path, -1)
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
        val mayaPySdks = PythonSdkUtil.getAllLocalCPythons().filter { x -> x.homePath?.endsWith(mayaPyExecutableName) ?: false }
        val homePaths = mayaPySdks.map { it.homePath!! }

        mayaSdkMapping.clear()

        for (path in homePaths) {
            if (state.mayaSdkMapping.containsKey(path)) {
                mayaSdkMapping[path] = state.mayaSdkMapping[path]!!
                continue
            }
            mayaSdkMapping[path] = SdkInfo(path, -1)
        }
        assignEmptyPorts()
    }

    fun refreshPythonSdks() {
        val mayaSdk = PythonSdkUtil.getAllLocalCPythons().filter { it.homePath?.endsWith(mayaPyExecutableName) ?: false }

        val homePathsSet = mayaSdk.map { it.homePath!! }.toSet()
        val sdkMappingKeySet = mayaSdkMapping.keys.toSet()

        val toAdd = homePathsSet - sdkMappingKeySet
        val toRemove = sdkMappingKeySet - homePathsSet

        for (path in toRemove) {
            mayaSdkMapping.remove(path)
        }

        for (path in toAdd) {
            mayaSdkMapping[path] = SdkInfo(path, -1)
        }
        assignEmptyPorts()
    }

    private fun assignEmptyPorts() {
        val usedPorts = mayaSdkMapping.map { it.value.port }.filter { it > 0 }.toSet()
        val freePorts = PriorityQueue((portRange - usedPorts).sorted())

        for (key in mayaSdkMapping.filter { it.value.port < 0 }.keys) {
            mayaSdkMapping[key]!!.port = freePorts.remove()
        }
    }

    fun getUnusedPort(): Int {
        val usedPorts = mayaSdkMapping.map { it.value.port }.filter { it > 0 }.toSet()
        val freePorts = PriorityQueue((portRange - usedPorts).sorted())
        return freePorts.remove()
    }
}
