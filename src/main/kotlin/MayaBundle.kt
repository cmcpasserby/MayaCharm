import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

class MayaBundle : DynamicBundle(BUNDLE) {
    companion object {
        private const val BUNDLE = "MayaCharmBundle"
        private val INSTANCE = MayaBundle()

        fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg parms: Any): String {
            return INSTANCE.getMessage(key, *parms)
        }
    }
}
