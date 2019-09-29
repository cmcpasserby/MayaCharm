package ca.rightsomegoodgames.mayacharm

import com.intellij.CommonBundle
import com.intellij.reference.SoftReference
import org.jetbrains.annotations.PropertyKey
import java.lang.ref.Reference
import java.util.*

object MayaBundle {
    private const val BUNDLE = "ca.rightsomegoodgames.mayacharm.MayaCharmBundle"
    private var ourBundle: Reference<ResourceBundle>? = null

    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg parms: Any): String {
        return CommonBundle.message(getBundle(), key, parms)
    }

    fun messageOfNull(@PropertyKey(resourceBundle = BUNDLE) key: String, vararg parms: Any): String? {
        return CommonBundle.messageOfNull(getBundle(), key, parms)
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
