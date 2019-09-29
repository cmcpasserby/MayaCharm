package ca.rightsomegoodgames.mayacharm.utils

interface Event<out TS> {
    operator fun plusAssign(m: (TS) -> Unit)
    operator fun minusAssign(m: (TS) -> Unit)
}

class Delegate<TS> : Event<TS> {
    private var invocationList: MutableList<(TS) -> Unit>? = null

    override fun plusAssign(m: (TS) -> Unit) {
        val list = invocationList ?: mutableListOf<(TS) -> Unit>().apply { invocationList = this }
        list.add(m)
    }

    override fun minusAssign(m: (TS) -> Unit) {
        val list = invocationList
        if (list != null) {
            list.remove(m)
            if (list.isEmpty()) {
                invocationList = null
            }
        }
    }

    operator fun invoke(source:TS) {
        val list = invocationList ?: return
        for (m in list) {
            m(source)
        }
    }
}
