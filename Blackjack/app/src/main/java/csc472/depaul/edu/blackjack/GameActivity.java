package csc472.depaul.edu.blackjack;

import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import cards.Card;
import cards.Deck;
import cards.FaceValue;
import cards.Suit;
import gameEntities.BlackjackHand;
import notifications.BlackjackNotificationKillService;
import notifications.BlackjackNotificationScheduler;
import notifications.BlackjackReminderNotificationFactory;
import stats.StatTracker;
import android.os.Vibrator;
import android.os.VibrationEffect;

public class GameActivity extends Activity {

    //variables for use in the game
    int betVal;
    int initialBet;
    int increment;
    int totalCash;
    int comPos = 0;
    int playerPos = 0;
    Deck deck;
    boolean hasBet = false;
    boolean hasStood = false;

    StatTracker statTracker;

    TextView[] playerViews;
    TextView[] comViews;
    Button[] buttons;
    Button[] endBtns;

    BlackjackHand playerHand;
    BlackjackHand comHand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        /* Create NotificationChannel for Reminders. */
        BlackjackNotificationScheduler.setUpReminderCommunicationChannel(this);
        /* Start Kill Service. */
        startService(new Intent(this, BlackjackNotificationKillService.class));

        /* Load current player funds from SharedPreferences and set totalCash accordingly. */
        SharedPreferences fundsFile = getSharedPreferences(getString(R.string.funds_file), MODE_PRIVATE);
        int savedCash = fundsFile.getInt(getString(R.string.funds_key), 1000);
        if (savedCash == 0) savedCash = 1000;
        totalCash = savedCash;

        increment = 100;

        /* Instantiate StatTracker. */
        statTracker = new StatTracker(this);

        if (savedInstanceState != null){
            onRestoreInstanceState(savedInstanceState);
            if (hasBet){
                initialBet = 100;
                setUpViews();
                setUpButtons();
                rebuildState();
            }
            else {
                initialBet = 100;
                setUpViews();
                setUpButtons();
                resetGame();
            }
        }

        else{

            betVal = 100;
            initialBet = 100;
            deck = new Deck();
            deck.shuffle();
            setUpViews();
            setUpButtons();
            resetGame();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        bundle.putInt("currBet",betVal);
        bundle.putBoolean("hasBet",hasBet);
        bundle.putParcelable("comHand",comHand);
        bundle.putParcelable("playerHand",playerHand);
        bundle.putParcelable("deck",deck);
        bundle.putBoolean("hasStood",hasStood);
        super.onSaveInstanceState(bundle);
    }

    public void onRestoreInstanceState(Bundle bundle){
        betVal = bundle.getInt("currBet");
        hasBet = bundle.getBoolean("hasBet");
        comHand = bundle.getParcelable("comHand");
        playerHand = bundle.getParcelable("playerHand");
        deck = bundle.getParcelable("deck");
        hasStood = bundle.getBoolean("hasStood");
        super.onRestoreInstanceState(bundle);
    }

    @Override
    public void onResume() {
        super.onResume();

        /* Create Notification for cancellation comparision and cancel. */
        Notification notificationToCancel = BlackjackReminderNotificationFactory.createNotification(this);
        BlackjackNotificationScheduler.cancelNotification(this, notificationToCancel);
    }

    @Override
    public void onPause() {
        super.onPause();

        /* Create Notification to send and how many seconds to wait before sending. */
        Notification reminderNotificationToSchedule = BlackjackReminderNotificationFactory.createNotification(this);
        int alarmTimeInSeconds = 30;
        /* Schedule Notification. */
        BlackjackNotificationScheduler.scheduleNotification(this, reminderNotificationToSchedule, alarmTimeInSeconds * 1000);
    }

    @Override
    public void onStop() {
        super.onStop();

    }

    @Override
    public void onDestroy() {
        /* Call superclass onDestroy() */
        super.onDestroy();
        /* Save current funds. */
        saveFunds();
        /* Cancel any pending notifications. */
        BlackjackNotificationScheduler.cancelNotification(this, BlackjackReminderNotificationFactory.createNotification(this));
    }


    protected void rebuildState(){
        for (TextView view : comViews){
            view.setVisibility(View.INVISIBLE);
            view.setText("");
        }
        for (TextView player : playerViews){
            player.setVisibility(View.INVISIBLE);
            player.setText("");
        }
        for (Card card : comHand.returnHand()){
            comViews[comPos].setText(buildCardString(card));
            comViews[comPos].setVisibility(View.VISIBLE);
            if (card.getSuit() == Suit.DIAMONDS || card.getSuit() == Suit.HEARTS) comViews[comPos].setTextColor(Color.RED);
            else comViews[comPos].setTextColor(Color.BLACK);
            comPos = comPos + 1;
        }
        for (Card card : playerHand.returnHand()){
            playerViews[playerPos].setText(buildCardString(card));
            playerViews[playerPos].setVisibility(View.VISIBLE);
            if (card.getSuit() == Suit.DIAMONDS || card.getSuit() == Suit.HEARTS) playerViews[playerPos].setTextColor(Color.RED);
            else playerViews[playerPos].setTextColor(Color.BLACK);
            playerPos = playerPos + 1;
        }
        Button betUp = findViewById(R.id.RaiseBet);
        Button betDown = findViewById(R.id.LowerBet);
        Button hit = findViewById(R.id.HitButton);
        Button stand = findViewById(R.id.StandButton);
        Button bet = findViewById(R.id.BetButton);
        bet.setEnabled(false);
        betUp.setEnabled(false);
        betDown.setEnabled(false);
        hit.setEnabled(true);
        stand.setEnabled(true);

        TextView handText = findViewById(R.id.HandValueView);
        handText.setText("Your hand: " + Integer.toString(playerHand.evaluateHand()));

        if (playerHand.evaluateHand() == 21 && comHand.evaluateHand() == 21) {
            TextView comHandText = findViewById(R.id.comValueView);
            comHandText.setText("Computer's hand: " + Integer.toString(comHand.evaluateHand()));
            TextView cover = findViewById(R.id.com1Cover);
            cover.setVisibility(View.INVISIBLE);

            /* Update stats. */
            statTracker.incrementPlayerBlackjacks();
            statTracker.incrementHouseBlackjacks();

            onPush();
        }
        else if (playerHand.evaluateHand() == 21){

            naturalBlackjack();
        }
        else if (comHand.evaluateHand() == 21){

            comBlackjack();
        }

        if (hasStood) onStand();

    }

    /*
        sets the initial values for the bank and the bet text
        create the TextView arrays used to display drawn cards
     */
    protected void setUpViews() {
        TextView betText = findViewById(R.id.BetText);
        TextView bankText = findViewById(R.id.BankText);
        betText.setText("Bet: $" + betVal);
        bankText.setText("Bank: $" + totalCash);
        playerViews = new TextView[]{
                findViewById(R.id.player1),
                findViewById(R.id.player2),
                findViewById(R.id.player3),
                findViewById(R.id.player4),
                findViewById(R.id.player5),
                findViewById(R.id.player6),
                findViewById(R.id.player7),
                findViewById(R.id.player8),
                findViewById(R.id.player9),
                findViewById(R.id.player10),
                findViewById(R.id.player11)};

        comViews = new TextView[]{
                findViewById(R.id.com1),
                findViewById(R.id.com2),
                findViewById(R.id.com3),
                findViewById(R.id.com4),
                findViewById(R.id.com5),
                findViewById(R.id.com6),
                findViewById(R.id.com7),
                findViewById(R.id.com8),
                findViewById(R.id.com9),
                findViewById(R.id.com10),
                findViewById(R.id.com11)};
    }

    /*
        create the button arrays used to enable and disable buttons
     */
    protected void setUpButtons(){
        buttons = new Button[]{
                findViewById(R.id.StandButton),
                findViewById(R.id.HitButton),
                findViewById(R.id.LowerBet),
                findViewById(R.id.RaiseBet),
                findViewById(R.id.BetButton)
        };
        endBtns = new Button[]{
                findViewById(R.id.nextHandBtn),
                findViewById(R.id.homeBtn)
        };
    }


    /*
        resets variables and views to a neutral state
     */
    protected void resetGame(){
        LinearLayout layout = findViewById(R.id.ResultsLayout);
        layout.setVisibility(View.INVISIBLE);
        comPos = 0;
        playerPos = 0;
        TextView cover = findViewById(R.id.com1Cover);
        cover.setVisibility(View.VISIBLE);
        for (TextView card : playerViews){
            card.setText("");
            card.setVisibility(View.INVISIBLE);
        }
        for (TextView card : comViews){
            card.setText("");
            card.setVisibility(View.INVISIBLE);
        }
        playerHand = new BlackjackHand();
        comHand = new BlackjackHand();
        deck = new Deck();
        deck.shuffle();
        Button betUp = findViewById(R.id.RaiseBet);
        Button betDown = findViewById(R.id.LowerBet);
        Button hit = findViewById(R.id.HitButton);
        Button stand = findViewById(R.id.StandButton);
        Button make = findViewById(R.id.BetButton);
        make.setEnabled(true);
        betUp.setEnabled(true);
        betDown.setEnabled(true);
        hit.setEnabled(false);
        stand.setEnabled(false);
        for (Button end : endBtns){
            end.setEnabled(false);
        }
        TextView player = findViewById(R.id.HandValueView);
        player.setText("Your hand: ");
        TextView comp = findViewById(R.id.comValueView);
        comp.setText("Computer's hand: ");
        if (betVal > totalCash){
            betVal = totalCash;
            TextView betText = findViewById(R.id.BetText);
            betText.setText("Bet: " + betVal);
        }
    }


    /*
        Draws the first two cards for the player and the house
     */
    protected void initialDraw(){
        Card card;
        int flag = 0;
        for (int i = 0; i < 4; i++){
            card = deck.dealCard();
            if (flag == 0){
                comHand.addCardToHand(card);
                TextView com = comViews[comPos];
                com.setText(buildCardString(card));
                com.setVisibility(View.VISIBLE);
                if (card.getSuit() == Suit.DIAMONDS || card.getSuit() == Suit.HEARTS) com.setTextColor(Color.RED);
                else com.setTextColor(Color.BLACK);
                flag = (flag + 1) % 2;
                comPos = comPos + 1;
            }
            else{
                playerHand.addCardToHand(card);
                TextView text = playerViews[playerPos];
                text.setText(buildCardString(card));
                text.setVisibility(View.VISIBLE);
                if (card.getSuit() == Suit.DIAMONDS || card.getSuit() == Suit.HEARTS) text.setTextColor(Color.RED);
                else text.setTextColor(Color.BLACK);
                flag = (flag + 1) % 2;
                playerPos = playerPos + 1;
            }
        }


        TextView handText = findViewById(R.id.HandValueView);
        handText.setText("Your hand: " + Integer.toString(playerHand.evaluateHand()));

        if (playerHand.evaluateHand() == 21 && comHand.evaluateHand() == 21) {
            TextView comHandText = findViewById(R.id.comValueView);
            comHandText.setText("Computer's hand: " + Integer.toString(comHand.evaluateHand()));
            TextView cover = findViewById(R.id.com1Cover);
            cover.setVisibility(View.INVISIBLE);

            /* Update stats. */
            statTracker.incrementPlayerBlackjacks();
            statTracker.incrementHouseBlackjacks();

            onPush();
        }
        else if (playerHand.evaluateHand() == 21){

            naturalBlackjack();
        }
        else if (comHand.evaluateHand() == 21){

            comBlackjack();
        }
    }


    /*
        Builds a string that can be used by a text view to display its suit and value
     */
    protected String buildCardString(Card card){
        StringBuilder sb = new StringBuilder();
        FaceValue value = card.getFaceValue();
        switch (value){
            case ACE: sb.append('A'); break;
            case TWO: sb.append('2'); break;
            case THREE: sb.append('3'); break;
            case FOUR: sb.append('4'); break;
            case FIVE: sb.append('5'); break;
            case SIX: sb.append('6'); break;
            case SEVEN: sb.append('7'); break;
            case EIGHT: sb.append('8'); break;
            case NINE: sb.append('9'); break;
            case TEN: sb.append("10"); break;
            case JACK: sb.append('J'); break;
            case QUEEN: sb.append('Q'); break;
            case KING: sb.append('K'); break;
        }

        sb.append("\n");

        Suit suit = card.getSuit();
        switch (suit){
            case CLUBS: sb.append("\u2663"); break;
            case HEARTS: sb.append("\u2665"); break;
            case SPADES: sb.append("\u2660"); break;
            case DIAMONDS: sb.append("\u2666"); break;
        }
        return sb.toString();
    }


    /*
        checks the player's hand for a natural blackjack
     */
    protected void naturalBlackjack(){
        for (Button btn : buttons){
            btn.setEnabled(false);
        }
        Button next = findViewById(R.id.nextHandBtn);
        next.setEnabled(true);
        Button home = findViewById(R.id.homeBtn);
        home.setEnabled(true);
        TextView bank = findViewById(R.id.BankText);

        /* Call vibration function. */
        blackjackVibration();

        totalCash = totalCash + (betVal * 2);
        bank.setText("Bank: $" + totalCash);
        LinearLayout layout = findViewById(R.id.ResultsLayout);
        layout.setVisibility(View.VISIBLE);
        TextView victory = findViewById(R.id.victoryText);
        victory.setText("BLACKJACK!");
        TextView losses = findViewById(R.id.amountText);
        losses.setText("You gained: $" + Integer.toString(betVal*2));

        /* Update and save stats. */
        statTracker.incrementPlayerBlackjacks();
        statTracker.incrementWins();
        statTracker.setMaxCash(totalCash);
        statTracker.setBiggestWin(betVal * 2);
        statTracker.saveStats(this);

    }

    protected void comBlackjack(){
        for (Button btn : buttons){
            btn.setEnabled(false);
        }
        TextView handText = findViewById(R.id.comValueView);
        handText.setText("Computer's hand: " + Integer.toString(comHand.evaluateHand()));
        TextView cover = findViewById(R.id.com1Cover);
        cover.setVisibility(View.INVISIBLE);
        Button next = findViewById(R.id.nextHandBtn);
        next.setEnabled(true);
        Button home = findViewById(R.id.homeBtn);
        home.setEnabled(true);
        TextView bank = findViewById(R.id.BankText);
        totalCash = totalCash - betVal;
        bank.setText("Bank: $" + totalCash);
        LinearLayout layout = findViewById(R.id.ResultsLayout);
        layout.setVisibility(View.VISIBLE);
        TextView victory = findViewById(R.id.victoryText);
        victory.setText("HOUSE BLACKJACK!");
        TextView losses = findViewById(R.id.amountText);
        losses.setText("You lost: $" + Integer.toString(betVal));

        /* Update and save stats. */
        statTracker.incrementHouseBlackjacks();
        statTracker.incrementLosses();
        statTracker.setBiggestLoss(betVal);
        statTracker.saveStats(this);
    }

    /*
        deals the player one card
        update the hand value TextView
        add a TextView for the card and populate it
        check if the player has lost
     */
    protected void onHit(){
        Card card = deck.dealCard();
        playerHand.addCardToHand(card);
        TextView text = playerViews[playerPos];
        text.setText(buildCardString(card));
        text.setVisibility(View.VISIBLE);
        if (card.getSuit() == Suit.DIAMONDS || card.getSuit() == Suit.HEARTS) text.setTextColor(Color.RED);
        else text.setTextColor(Color.BLACK);
        playerPos = playerPos + 1;
        TextView handVal = findViewById(R.id.HandValueView);
        handVal.setText("Your Hand: " + Integer.toString(playerHand.evaluateHand()));
        if (evaluateLoss()) {
            onLoss();
        }
    }


    /*
        draw cards for the computer until their hand > 16 in value
        compare the computer's hand value to the player's hand value
        call the player loss or computer loss function, based on previous check
     */
    protected void onStand(){
        hasStood = true;
        TextView handText = findViewById(R.id.comValueView);
        handText.setText("Computer's hand: " + Integer.toString(comHand.evaluateHand()));
        TextView cover = findViewById(R.id.com1Cover);
        cover.setVisibility(View.INVISIBLE);
        while (comHand.evaluateHand() < 17){
            Card card = deck.dealCard();
            comHand.addCardToHand(card);
            TextView text = comViews[comPos];
            text.setText(buildCardString(card));
            text.setVisibility(View.VISIBLE);
            if (card.getSuit() == Suit.DIAMONDS || card.getSuit() == Suit.HEARTS) text.setTextColor(Color.RED);
            else text.setTextColor(Color.BLACK);
            comPos = comPos + 1;
            handText.setText("Computer's hand: " + Integer.toString(comHand.evaluateHand()));
        }
        if (comHand.evaluateHand() == playerHand.evaluateHand()) {
            onPush();
        }
        else if (comHand.evaluateHand() > 21) {
            /* Update house bust stat. */
            statTracker.incrementHouseBusts();
            comLoss();
        }
        else if (comHand.evaluateHand() < playerHand.evaluateHand()) {
            comLoss();
        }
        else{
            onLoss();
        }
    }


    protected void onPush() {
        for (Button btn : buttons){
            btn.setEnabled(false);
        }
        for (Button end : endBtns){
            end.setEnabled(true);
        }
        LinearLayout layout = findViewById(R.id.ResultsLayout);
        layout.setVisibility(View.VISIBLE);
        TextView victory = findViewById(R.id.victoryText);
        victory.setText("It's a push!");
        TextView losses = findViewById(R.id.amountText);
        losses.setText("You keep your bet!");

        /* Update and save stats. */
        statTracker.incrementPushes();
        statTracker.saveStats(this);
    }

    /*
        executes when the computer loses
        increases the bank of the player by the bet amount
        disables the betting, hit and stand buttons
        enables the ending buttons
        displays the results popup
     */
    protected void comLoss(){
        totalCash = totalCash + betVal;


        for (Button btn : buttons){
            btn.setEnabled(false);
        }
        for (Button end : endBtns){
            end.setEnabled(true);
        }
        

        TextView bank = findViewById(R.id.BankText);
        bank.setText("Bank: $" + totalCash);
        LinearLayout layout = findViewById(R.id.ResultsLayout);
        layout.setVisibility(View.VISIBLE);
        TextView victory = findViewById(R.id.victoryText);
        victory.setText("Victory");
        TextView losses = findViewById(R.id.amountText);
        losses.setText("You gained: $" + Integer.toString(betVal));

        /* Update and save stats. */
        statTracker.incrementWins();
        statTracker.setMaxCash(totalCash);
        statTracker.setBiggestWin(betVal);
        statTracker.saveStats(this);
    }

    /*
        checks the player's hand for a loss condition (hand value > 21)
        returns a boolean indicating a loss
     */
    protected boolean evaluateLoss(){
        if (playerHand.evaluateHand() > 21){
            /* Update player bust stat. */
            statTracker.incrementPlayerBusts();
            return true;
        }
        return false;
    }


    /*
        executes when a player loses
        disables the betting, hit, and stand buttons
        enables the end of game buttons
        displays a results popup
     */
    protected void onLoss(){
        totalCash = totalCash - betVal;
        for (Button btn : buttons){
            btn.setEnabled(false);
        }
        for (Button end : endBtns){
            end.setEnabled(true);
        }

        TextView bank = findViewById(R.id.BankText);
        bank.setText("Bank: $" + totalCash);

        /* Update stats. */
        statTracker.setBiggestLoss(betVal);
        statTracker.incrementLosses();

        if (totalCash == 0){
            /* Update bankruptcy stat. */
            statTracker.incrementBankruptcies();

            LinearLayout layout = findViewById(R.id.ResultsLayout);
            layout.setVisibility(View.VISIBLE);
            TextView victory = findViewById(R.id.victoryText);
            victory.setText("BANKRUPT");
            TextView losses = findViewById(R.id.amountText);
            losses.setText("You lost: $" + Integer.toString(betVal));
            Button replay = findViewById(R.id.nextHandBtn);
            replay.setText("Restart");
        }
        else {
            LinearLayout layout = findViewById(R.id.ResultsLayout);
            layout.setVisibility(View.VISIBLE);
            TextView victory = findViewById(R.id.victoryText);
            victory.setText("DEFEAT");
            TextView losses = findViewById(R.id.amountText);
            losses.setText("You lost: $" + Integer.toString(betVal));
        }
        statTracker.saveStats(this);
    }



    /*
        Button handler for the raise bet button
        Increments the value of bet by increment if the new value is less than
            or equal to the player's bank
     */
    protected void onRaiseBet(){
        if (betVal + increment <= totalCash) {
            TextView betText = findViewById(R.id.BetText);
            betVal = betVal + increment;
            betText.setText("Bet: $" + betVal);
        }
    }


    /*
        Button handler for the lower bet button
        Decrements the value of bet by increment if the new value is greater than
            or equal to the value of the increment
     */
    protected void onLowerBet(){
        if (betVal > increment) {
            TextView betText = findViewById(R.id.BetText);
            betVal = betVal - increment;
            betText.setText("Bet: $" + betVal);
        }
    }

    protected void onBet(Button btn){
        Button betUp = findViewById(R.id.RaiseBet);
        Button betDown = findViewById(R.id.LowerBet);
        Button hit = findViewById(R.id.HitButton);
        Button stand = findViewById(R.id.StandButton);
        btn.setEnabled(false);
        betUp.setEnabled(false);
        betDown.setEnabled(false);
        hit.setEnabled(true);
        stand.setEnabled(true);
        hasBet = true;
        initialDraw();
    }


    /*
        helper function for onRaiseBet
     */
    public void raiseBet(View v){
        onRaiseBet();
    }

    /*
        helper function for onLowerBet
     */
    public void lowerBet(View v){
        onLowerBet();
    }


    /*
        helper function for onHit
     */
    public void hit(View v){
        onHit();
    }

    public void stand(View v){
        onStand();
    }

    /*
        begins a new game, only clickable when results are displayed
     */
    public void nextHand(View v){
        Button btn = (Button) v;
        if (btn.getText() == "Restart"){
            btn.setText("Next Hand");
            totalCash = 1000;

            /* Save reset funds to files for next game. */
            saveFunds();

            betVal = initialBet;
            TextView bank = findViewById(R.id.BankText);
            bank.setText("Bank: $" + totalCash);
            TextView bet = findViewById(R.id.BetText);
            bet.setText("Bet: $" + betVal);
        }
        Button bet = findViewById(R.id.BetButton);
        bet.setEnabled(true);
        hasBet = false;
        hasStood = false;
        resetGame();
    }

    /*
        calls the finish() method, only clickable when results are displayed
     */
    public void homePress(View v){
        saveFunds();
        statTracker.saveStats(this);
        finish();
    }

    public void bet(View v){
        Button btn = (Button) v;
        onBet(btn);
    }

    /* Clears currently saved funds from Shared Preferences. */
    public void clearSavedFunds() {
        SharedPreferences fundsFile = getSharedPreferences(getString(R.string.funds_file), MODE_PRIVATE);
        Editor fundsFileEditor = fundsFile.edit();

        fundsFileEditor.clear();
        fundsFileEditor.commit();
    }

    /* Saves current funds to Shared Preferences. */
    public void saveFunds() {
        SharedPreferences fundsFile = getSharedPreferences(getString(R.string.funds_file), MODE_PRIVATE);
        Editor fundsFileEditor = fundsFile.edit();

        fundsFileEditor.putInt(getString(R.string.funds_key), totalCash);
        fundsFileEditor.commit();

    }

    /* Private function that encapsulates creation of vibrator class, checks the Android version of the
     * device the app is being run on, and calls the appropriate vibration method. */
    private void blackjackVibration() {
        /* Instantiate Vibrator. */
        Vibrator blackjackVib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        /* vibrate method which takes a millisecond parameter depreciated in Android 26 (Oreo).  New method
         * takes a VibrationEffect, which specifies time and amplitude.  Check the Android version of the device.
         * If Oreo or higher, call new vibrate method.  If lower, call depreciated method. */
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            /* Android version is 26 or higher, call new vibration method by passing it a new VibrationEffect. */
            blackjackVib.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
        }
        else {
            /* Android version is 25 or lower, call depreciated method. */
            blackjackVib.vibrate(500);
        }

    }

}
