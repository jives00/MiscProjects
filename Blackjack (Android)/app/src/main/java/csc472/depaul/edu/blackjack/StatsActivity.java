package csc472.depaul.edu.blackjack;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import notifications.BlackjackNotificationScheduler;
import notifications.BlackjackReminderNotificationFactory;
import stats.StatTracker;

public class StatsActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);

        Intent intent = getIntent();

        /* Instantiate StatTracker. */
        StatTracker statTracker = new StatTracker(this);

        if (statTracker != null) {
            /* Set TextViews to current Stats in statTracker. */

            TextView maxCashView = findViewById(R.id.TopMoneyText);
            maxCashView.setText("Most Money: $" + Integer.toString(statTracker.getMaxCash()));

            TextView biggestBetView = findViewById(R.id.TopBetText);
            biggestBetView.setText("Biggest Win: $" + Integer.toString(statTracker.getBiggestWin()));

            TextView biggestLossView = findViewById(R.id.BiggestLossText);
            biggestLossView.setText("Biggest Loss: $" + Integer.toString(statTracker.getBiggestLoss()));

            TextView totalWinsText = findViewById(R.id.totalWinsText);
            totalWinsText.setText("Total Wins: " + Integer.toString(statTracker.getTotalWins()));

            TextView totalLossesText = findViewById(R.id.totalLossesText);
            totalLossesText.setText("Total Losses: " + Integer.toString(statTracker.getTotalLosses()));

            TextView totalPushesText = findViewById((R.id.totalPushesText));
            totalPushesText.setText("Total Pushes: " + Integer.toString(statTracker.getTotalPushes()));

            TextView winPercentageText = findViewById(R.id.winPercentageText);
            winPercentageText.setText("Win Percentage: " +  String.format("%.2f", statTracker.getWinPercentage()) + "%");

            TextView playerBlackjacksText = findViewById(R.id.playerBlackjacksText);
            playerBlackjacksText.setText("Player Blackjacks: " + Integer.toString(statTracker.getNumberOfPlayerBlackjacks()));

            TextView houseBlackjacksText = findViewById(R.id.houseBlackjacksText);
            houseBlackjacksText.setText("House Blackjacks: " + Integer.toString(statTracker.getNumberOfHouseBlackjacks()));

            TextView playerBustsText = findViewById(R.id.playerBustsText);
            playerBustsText.setText("Player Busts: " + Integer.toString(statTracker.getTotalPlayerBusts()));

            TextView houseBustsText = findViewById(R.id.houseBustsText);
            houseBustsText.setText("House Busts: " + Integer.toString(statTracker.getTotalHouseBusts()));

            TextView totalBankruptciesText = findViewById((R.id.totalBankruptciesText));
            totalBankruptciesText.setText("Times Bankrupt: " + Integer.toString(statTracker.getTotalBankruptcies()));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        /* Cancel any pending notifications. */
        BlackjackNotificationScheduler.cancelNotification(this, BlackjackReminderNotificationFactory.createNotification(this));
    }
}
