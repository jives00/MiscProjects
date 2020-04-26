package csc472.depaul.edu.blackjack;

import android.app.Activity;
import android.os.Bundle;

import notifications.BlackjackNotificationScheduler;
import notifications.BlackjackReminderNotificationFactory;

public class CreditsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credits);
    }

    @Override
    public void onResume() {
        super.onResume();
        /* Cancel any pending notifications. */
        BlackjackNotificationScheduler.cancelNotification(this, BlackjackReminderNotificationFactory.createNotification(this));
    }
}
