package utils

interface Event<out TA> {
    operator fun plusAssign(m: (TA) -> Unit)
    operator fun minusAssign(m: (TA) -> Unit)
}

class Delegate<TA> : Event<TA> {
    private var invocationList: MutableList<(TA) -> Unit>? = null

    override fun plusAssign(m: (TA) -> Unit) {
        invocationList = invocationList ?: mutableListOf(m)
    }

    override fun minusAssign(m: (TA) -> Unit) {
        invocationList?.let {
            it.remove(m)
            if (it.isEmpty()) {
                invocationList = null
            }
        }
    }

    operator fun invoke(source: TA) = invocationList?.forEach { it(source) }
}
