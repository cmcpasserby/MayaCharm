import com.intellij.CommonBundle
import com.intellij.reference.SoftReference
import org.jetbrains.annotations.PropertyKey
import java.lang.ref.Reference
import java.util.*

object MayaBundle {
    private const val BUNDLE = "MayaCharmBundle"
    private var ourBundle: Reference<ResourceBundle>? = null

    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg parms: Any): String {
        return CommonBundle.message(getBundle(), key, *parms)
    }

    fun messageOrNull(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg parms: Any): String? {
        return CommonBundle.messageOrNull(getBundle(), key, *parms)
    }

    private fun getBundle(): ResourceBundle {
        var bundle = SoftReference.dereference(ourBundle)

        if (bundle == null) {
            bundle = ResourceBundle.getBundle(BUNDLE)
            ourBundle = SoftReference(bundle)
        }
        return bundle!!
    }
}
