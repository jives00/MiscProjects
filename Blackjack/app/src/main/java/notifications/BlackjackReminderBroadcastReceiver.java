package notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/* Custom BroadcastReceiver class that will receive broadcasts from the AlarmManager, along with
* attached Intents.  It's onReceive method will unpack the Notification from the received Intent
* and send it with the NotificationManager. */
public class BlackjackReminderBroadcastReceiver extends BroadcastReceiver {

    public static String ID_KEY = "reminder_id";
    public static String NOTIFICATION_KEY = "reminder_notification";

    @Override
    public void onReceive(Context context, Intent intent) {
        /* Instantiate NotificationManager from parameter context. */
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        /* Unmarshall notification and it's ID from Intent parameter. */
        Notification reminderNotification = intent.getParcelableExtra(NOTIFICATION_KEY);
        int id = intent.getIntExtra(ID_KEY, 0);

        /* Send notification to notification channel. */
        notificationManager.notify(id, reminderNotification);
    }
}
