package ca.rightsomegoodgames.mayacharm.resources

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType

object MayaNotifications {
    val CONNECTION_REFUSED = Notification("MayaCharm", "MayaCharm",
            "Connection to Maya refused", NotificationType.ERROR)

    val FILE_FAIL = Notification("MayaCharm", "MayaCharm",
            "Failed to create tempFile", NotificationType.ERROR)
}
