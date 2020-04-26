package notifications;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import csc472.depaul.edu.blackjack.GameActivity;
import csc472.depaul.edu.blackjack.R;

/* Static factory class that builds Notifications. */
public class BlackjackReminderNotificationFactory {

    private BlackjackReminderNotificationFactory() {}

    public static Notification createNotification(Context context) {
        /* Create PendingIntent to attach to notification click. */
        Intent gameActivityIntent = new Intent(context, GameActivity.class);
        gameActivityIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent gameActivityPendingIntent = PendingIntent.getActivity(context, 0, gameActivityIntent, 0);

        /* Build Notification. */
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, BlackjackNotificationScheduler.REMINDER_CHANNEL);
        notificationBuilder.setContentTitle("Our dealers miss you...");
        notificationBuilder.setContentText("They can't pay themselves you know.");
        notificationBuilder.setSmallIcon(R.drawable.ic_launcher_foreground);
        notificationBuilder.setContentIntent(gameActivityPendingIntent);
        notificationBuilder.setAutoCancel(true);
        return notificationBuilder.build();

    }
}
