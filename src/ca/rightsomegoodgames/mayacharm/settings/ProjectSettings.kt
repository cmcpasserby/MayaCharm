package ca.rightsomegoodgames.mayacharm.settings

import com.intellij.openapi.components.*
import com.intellij.openapi.project.Project
import com.intellij.util.io.exists
import java.nio.file.Paths

@State(
        name = "MCProjectSettings",
        storages = [Storage(value = StoragePathMacros.WORKSPACE_FILE)]
)
class ProjectSettings : PersistentStateComponent<ProjectSettings.State> {
    data class State(var Port: Int = 0, var Host: String = "")
    private var myState = State()

    override fun getState(): State? {
        return myState
    }

    override fun loadState(state: State) {
        myState.Host = state.Host
        myState.Port = state.Port
    }

    var port: Int
        get() = if (myState.Port < 1) 4434 else myState.Port
        set(value) {myState.Port = value}

    var host: String
        get() = if  (myState.Host.isBlank()) "localhost" else myState.Host
        set(value) {myState.Host = value}

    companion object {
        public fun getInstance(project: Project):ProjectSettings {
            return ServiceManager.getService(project, ProjectSettings::class.java)
        }

        public fun MayaPyFromMaya(path: String): String? {
            val p = Paths.get(path)

            // todo figure out windows vs mac pathing here
            if (p.fileName.toString() != "maya.exe") {
                return null
            }

            // todo figure out windows vs mac pathing here
            val newPath = p.parent.resolve("mayapy.exe")
            return if (newPath.exists()) newPath.toString() else null
        }

        public fun MayaFromMayaPy(path: String): String? {
            val p = Paths.get(path)

            // todo figure out windows vs mac pathing here
            if (p.fileName.toString() != "mayapy.exe") {
                return null
            }

            // todo figure out windows vs mac pathing here
            val newPath = p.parent.resolve("maya.exe")
            return if (newPath.exists()) newPath.toString() else null
        }
    }
}
