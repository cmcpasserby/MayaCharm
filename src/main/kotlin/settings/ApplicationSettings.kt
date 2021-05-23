package settings

import com.intellij.openapi.components.*
import com.jetbrains.python.sdk.PythonSdkUtil
import mayacomms.MayaPyVersion
import mayacomms.getMayaPyVersion
import mayacomms.isValidMayaPy
import mayacomms.mayaFromMayaPy
import java.util.*

typealias SdkPortMap = MutableMap<String, ApplicationSettings.SdkInfo>

private val portRange = (4434..4534).toSet()

@State(
    name = "MCAppSettings",
    storages = [Storage(value = "mayacharm.settings.xml", roamingType = RoamingType.DISABLED)]
)
class ApplicationSettings : PersistentStateComponent<ApplicationSettings.State> {
    data class SdkInfo(
        val mayaPath: String,
        var mayaPy2Path: String? = null,
        var mayaPy3Path: String? = null,
        var port: Int = -1,
    )

    data class State(
        var mayaSdkMapping: SdkPortMap = mutableMapOf()
    )

    private val myState = State()

    companion object {
        val INSTANCE: ApplicationSettings
            get() = service()
    }

    init {
        val sdks = PythonSdkUtil.getAllLocalCPythons()
            .filter { sdk -> sdk.homePath?.let{ isValidMayaPy(it) } ?: false }

        for (sdk in sdks) {
            val homePath = sdk.homePath ?: continue
            val mayaPath = mayaFromMayaPy(homePath) ?: continue
            val versionString = sdk.versionString ?: continue
            val mayaPyVersion = getMayaPyVersion(versionString)

            if (mayaSdkMapping.containsKey(mayaPath)) {
                when (mayaPyVersion) {
                    MayaPyVersion.Py2 -> mayaSdkMapping[mayaPath]?.mayaPy2Path = homePath
                    MayaPyVersion.Py3 -> mayaSdkMapping[mayaPath]?.mayaPy3Path = homePath
                    else -> continue
                }
            }
            else {
                when (mayaPyVersion) {
                    MayaPyVersion.Py2 -> mayaSdkMapping[mayaPath] = SdkInfo(mayaPath = mayaPath, mayaPy2Path = homePath)
                    MayaPyVersion.Py3 -> mayaSdkMapping[mayaPath] = SdkInfo(mayaPath = mayaPath, mayaPy3Path = homePath)
                    else -> continue
                }
            }
        }

        assignEmptyPorts()
    }

    var mayaSdkMapping: SdkPortMap
        get() = myState.mayaSdkMapping
        set(value) {
            myState.mayaSdkMapping = value
        }

    override fun getState(): State = myState

    override fun loadState(state: State) {
        val mayaPySdks =
            PythonSdkUtil.getAllLocalCPythons().filter { it.homePath?.endsWith(mayaPyExecutableName) ?: false }
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
        val mayaSdk = PythonSdkUtil.getAllLocalCPythons()
            .filter { sdk -> sdk.homePath?.let{ isValidMayaPy(it) } ?: false }
            .map { mayaFromMayaPy(it.homePath!!) }

        val homePathsSet = mayaSdk.toSet()
        val sdkMappingKeySet = mayaSdkMapping.keys.toSet()

        val toAdd = homePathsSet - sdkMappingKeySet
        val toRemove = sdkMappingKeySet - homePathsSet

        toRemove.forEach { mayaSdkMapping.remove(it) }
        toAdd.forEach { mayaSdkMapping[it] = SdkInfo(path, -1) }

        assignEmptyPorts()
    }

    private fun assignEmptyPorts() {
        val usedPorts = mayaSdkMapping.map { it.value.port }.filter { it > 0 }.toSet()
        val freePorts = PriorityQueue((portRange - usedPorts).sorted())

        for (key in mayaSdkMapping.filter { it.value.port < 0 }.keys) {
            mayaSdkMapping[key]!!.port = freePorts.remove()
        }
    }
}
