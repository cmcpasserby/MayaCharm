package ca.rightsomegoodgames.mayacharm.utils

interface Event<out TA> {
    operator fun plusAssign(m: (TA) -> Unit)
    operator fun minusAssign(m: (TA) -> Unit)
}

class Delegate<TA> : Event<TA> {
    private var invocationList: MutableList<(TA) -> Unit>? = null

    override fun plusAssign(m: (TA) -> Unit) {
        val list = invocationList ?: mutableListOf<(TA) -> Unit>().apply { invocationList = this }
        list.add(m)
    }

    override fun minusAssign(m: (TA) -> Unit) {
        val list = invocationList
        if (list != null) {
            list.remove(m)
            if (list.isEmpty()) {
                invocationList = null
            }
        }
    }

    operator fun invoke(source:TA) {
        val list = invocationList ?: return
        for (m in list) {
            m(source)
        }
    }
}
