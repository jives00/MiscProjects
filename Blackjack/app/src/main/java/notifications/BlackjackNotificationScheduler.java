package notifications;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;

/* Static library class of methods to schedule and cancel notifications, as well as set up a
* NotificationChannel for Blackjack Reminder Notifications. */
public class BlackjackNotificationScheduler {

    public static String REMINDER_CHANNEL = "blackjack_reminder_channel";

    private BlackjackNotificationScheduler() {}

    public static void scheduleNotification(Context context, Notification notificationToSchedule, int alarmTime) {
        /* Create PendingIntent to to schedule with AlarmManager and package notification to schedule in the PendingIntent. */
        Intent broadcastIntent = new Intent(context, BlackjackReminderBroadcastReceiver.class);
        broadcastIntent.putExtra(BlackjackReminderBroadcastReceiver.ID_KEY, 1);
        broadcastIntent.putExtra(BlackjackReminderBroadcastReceiver.NOTIFICATION_KEY, notificationToSchedule);
        PendingIntent pendingIntentToBroadcast = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        /* Set time to wait before broadcast. */
        long timeToSend = SystemClock.elapsedRealtime() + alarmTime;

        /* Instantiate AlarmManager and schedule broadcast of PendingIntent. */
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, timeToSend, pendingIntentToBroadcast);
    }

    public static void cancelNotification(Context context, Notification notificationToCancel) {
        /* Create PendingIntent for AlarmManager's comparision for cancellation. */
        Intent broadcastIntent = new Intent(context, BlackjackReminderBroadcastReceiver.class);
        broadcastIntent.putExtra(BlackjackReminderBroadcastReceiver.ID_KEY, 1);
        broadcastIntent.putExtra(BlackjackReminderBroadcastReceiver.NOTIFICATION_KEY, notificationToCancel);
        PendingIntent pendingIntentToCancel = PendingIntent.getBroadcast(context, 0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        /* Instantiate and cancel any alarm with a matching PendingIntent. */
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(pendingIntentToCancel);
    }

    public static void setUpReminderCommunicationChannel(Context context) {
        /* Only required for Android 26 and above, so check the device's Android version and do nothing if below. */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Set channel variables. */
            String channelId = REMINDER_CHANNEL;
            CharSequence name = "BLACKJACK_REMINDER_CHANNEL";
            String description = "Channel for Blackjack Game Reminders";

            /* Instantiate NotificationChannel. */
            NotificationChannel blackjackReminderChannel = new NotificationChannel(channelId, name, NotificationManager.IMPORTANCE_DEFAULT);
            blackjackReminderChannel.setDescription(description);

            /* Create NotificationChannel with NotificationManager. */
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(blackjackReminderChannel);
        }
    }

}
