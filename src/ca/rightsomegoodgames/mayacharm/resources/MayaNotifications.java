package ca.rightsomegoodgames.mayacharm.resources;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;

public final class MayaNotifications {
    public static final Notification CONNECTION_REFUSED = new Notification("MayaCharm", "MayaCharm",
            "Connection to Maya refused", NotificationType.ERROR);

    public static final Notification FILE_FAIL = new Notification("MayaCharm", "MayaCharm",
            "Failed to create tempFile", NotificationType.ERROR);
}
