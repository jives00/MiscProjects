package notifications;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

/* Custom service responsible for cancelling any outstanding reminder notifications whenever
* the GameActivity task (the Activity it is started in) is removed from the stack. */
public class BlackjackNotificationKillService extends Service {

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /* onTaskRemoved instantiates the NotificationManager and cancels any broadcasts with an id of
     * 1 (1 is the ID being used for BlackjackNotification broadcasts). */
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(1);
    }

}
