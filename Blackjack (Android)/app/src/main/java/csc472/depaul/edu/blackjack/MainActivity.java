package csc472.depaul.edu.blackjack;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import notifications.BlackjackNotificationScheduler;
import notifications.BlackjackReminderNotificationFactory;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void click(View v){
        Intent intent;
        switch(v.getId()) {
            case R.id.StatsText:
                intent = new Intent(this, StatsActivity.class);
                break;
            case R.id.PlayText:
                intent = new Intent(this, GameActivity.class);
                break;
            case R.id.CreditText:
                intent = new Intent(this, CreditsActivity.class);
                break;
            default:
                intent = new Intent();
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        /* Cancel any pending notifications. */
        BlackjackNotificationScheduler.cancelNotification(this, BlackjackReminderNotificationFactory.createNotification(this));
    }

}
