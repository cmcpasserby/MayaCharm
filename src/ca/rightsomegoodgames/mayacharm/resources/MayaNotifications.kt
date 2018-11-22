package ca.rightsomegoodgames.mayacharm.resources

import com.intellij.notification.Notification
import com.intellij.notification.NotificationType

private const val displayGroup = "MayaCharm"
private const val titleText = "MayaCharm"

object MayaNotifications {
    val CONNECTION_REFUSED = Notification(displayGroup, titleText,
            "Connection to Maya refused", NotificationType.ERROR)

    val FILE_FAIL = Notification(displayGroup, titleText,
            "Failed to create tempFile", NotificationType.ERROR)

    val NO_SDK_SELECTED = Notification(displayGroup, titleText,
            "No Maya SDK is selected", NotificationType.ERROR)
}
