package resources

import MayaBundle as Loc
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.openapi.project.Project

private const val displayGroup = "MayaCharm"
private const val titleText = "MayaCharm"

object MayaNotifications {
    val CONNECTION_REFUSED = Notification(
        displayGroup, titleText,
            Loc.message("mayacharm.notifications.ConnectionRefused"), NotificationType.ERROR)

    val FILE_FAIL = Notification(
        displayGroup, titleText,
            Loc.message("mayacharm.notifications.FailedToCreateTempFile"), NotificationType.ERROR)

    val NO_SDK_SELECTED = Notification(
        displayGroup, titleText,
            Loc.message("mayacharm.notifications.NoSdkSelected"), NotificationType.ERROR)

    fun mayaInstanceNotFound(instancePath: String, project: Project) {
        Notification(
            displayGroup,
            titleText,
            Loc.message("mayacharm.notifications.NoRunningMayaInstanceFor", instancePath),
            NotificationType.ERROR).notify(project)
    }
}
