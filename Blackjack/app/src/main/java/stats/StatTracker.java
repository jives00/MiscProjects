package stats;

import android.content.Context;
import static android.content.Context.MODE_PRIVATE;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import csc472.depaul.edu.blackjack.R;

public class StatTracker {
    /* Fields for stats */
    private int maxCash;
    private int biggestWin;
    private int biggestLoss;
    private int totalWins;
    private int totalLosses;
    private int totalPushes;
    private float winPercentage;
    private int playerBlackjacks;
    private int houseBlackjacks;
    private int totalPlayerBusts;
    private int totalHouseBusts;
    private int totalBankruptcies;

    /* Constructor opens the stats Shared Preference file and sets each field to the appropriate
    * value if present in the file, otherwise sets a field to the specified default value. */
    public StatTracker(Context activityContext) {

        /* Create SharedPreferences object from file (or create initial file if this is the first time this class is being created. */
        SharedPreferences statsFile = activityContext.getSharedPreferences(activityContext.getString(R.string.stats_file), MODE_PRIVATE);

        /* Set fields from file contents, or use the specified default values. */
        this.maxCash = statsFile.getInt(activityContext.getString(R.string.max_cash_key), 1000);
        this.biggestWin = statsFile.getInt(activityContext.getString(R.string.biggest_win_key), 0);
        this.biggestLoss = statsFile.getInt(activityContext.getString(R.string.biggest_loss_key), 0);
        this.totalWins = statsFile.getInt(activityContext.getString(R.string.total_wins_key), 0);
        this.totalLosses = statsFile.getInt(activityContext.getString(R.string.total_losses_key), 0);
        this.totalPushes = statsFile.getInt(activityContext.getString(R.string.total_pushes_key), 0);

        /* If no games have been won, set win percentage to 0.*/
        if (this.totalWins == 0) {
            this.winPercentage = 0;
        }
        /* Otherwise, calculate win percentage from relevant stats. */
        else {
            /* Cast relevant integer stats to floats for proper calculation. */
            float floatWins = (float) this.totalWins;
            float floatLosses = (float) this.totalLosses;
            float floatPushes = (float) totalPushes;
            /* Calculate percentage. */
            this.winPercentage = (floatWins / (floatWins + floatLosses + floatPushes)) * 100;
        }

        this.playerBlackjacks = statsFile.getInt(activityContext.getString(R.string.player_blackjacks_key), 0);
        this.houseBlackjacks = statsFile.getInt(activityContext.getString(R.string.house_blackjacks_key), 0);
        this.totalPlayerBusts = statsFile.getInt(activityContext.getString(R.string.player_busts_key), 0);
        this.totalHouseBusts = statsFile.getInt(activityContext.getString(R.string.house_busts_key), 0);
        this.totalBankruptcies = statsFile.getInt(activityContext.getString(R.string.total_bankruptcies_key), 0);

    }

    /* Saves fields to stats SharedPreference file.  Needs context (the activity it is created in) as a parameter,
    * necessary to call getSharedPreferences.*/
    public void saveStats(Context activityContext) {

        /* Open file and get editor. */
        SharedPreferences statsFile = activityContext.getSharedPreferences(activityContext.getString(R.string.stats_file), MODE_PRIVATE);
        Editor statsFileEditor = statsFile.edit();

        /* Write data to file. */
        statsFileEditor.putInt(activityContext.getString(R.string.max_cash_key), maxCash);
        statsFileEditor.putInt(activityContext.getString(R.string.biggest_win_key), biggestWin);
        statsFileEditor.putInt(activityContext.getString(R.string.biggest_loss_key), biggestLoss);
        statsFileEditor.putInt(activityContext.getString(R.string.total_wins_key), totalWins);
        statsFileEditor.putInt(activityContext.getString(R.string.total_losses_key), totalLosses);
        statsFileEditor.putInt(activityContext.getString(R.string.total_pushes_key), totalPushes);
        statsFileEditor.putInt(activityContext.getString(R.string.player_blackjacks_key), playerBlackjacks);
        statsFileEditor.putInt(activityContext.getString(R.string.house_blackjacks_key), houseBlackjacks);
        statsFileEditor.putInt(activityContext.getString(R.string.player_busts_key), totalPlayerBusts);
        statsFileEditor.putInt(activityContext.getString(R.string.house_busts_key), totalHouseBusts);
        statsFileEditor.putInt(activityContext.getString(R.string.total_bankruptcies_key), totalBankruptcies);

        /* Commit changes. */
        statsFileEditor.commit();

    }

    /* Clears the currently saved stats. */
    public void clearSavedStats(Context activityContext) {
        SharedPreferences statsFile = activityContext.getSharedPreferences(activityContext.getString(R.string.stats_file), MODE_PRIVATE);
        Editor statsFileEditor = statsFile.edit();

        statsFileEditor.clear();
        statsFileEditor.commit();
    }

    /* Getters and various setters.  Setters either take a parameter, perform a test against the
    * current value of a field, and set the value to a new parameter if a condition is satisfied, or
    * they increment a value.*/
    public int getMaxCash() {
        return this.maxCash;
    }

    public void setMaxCash(int totalCash) {
        if (totalCash > maxCash) {
            this.maxCash = totalCash;
        }
    }

    public int getBiggestWin() {
        return biggestWin;
    }

    public void setBiggestWin(int bet) {
        if (bet > this.biggestWin) {
            this.biggestWin = bet;
        }
    }

    public int getBiggestLoss() {
        return biggestLoss;
    }

    public void setBiggestLoss(int bet) {
        if (bet > this.biggestLoss) {
            this.biggestLoss = bet;
        }
    }

    public int getTotalWins() {
        return totalWins;
    }

    public void incrementWins() { this.totalWins++; }

    public int getTotalLosses() {
        return totalLosses;
    }

    public void incrementLosses() { this.totalLosses++; }

    public int getTotalPushes() {
        return totalPushes;
    }

    public void incrementPushes() { this.totalPushes++; }

    public float getWinPercentage() {
        return winPercentage;
    }

    public int getNumberOfPlayerBlackjacks() {
        return playerBlackjacks;
    }

    public void incrementPlayerBlackjacks() { this.playerBlackjacks++; }

    public int getNumberOfHouseBlackjacks() {
        return houseBlackjacks;
    }

    public void incrementHouseBlackjacks() { this.houseBlackjacks++; }

    public int getTotalPlayerBusts() {
        return totalPlayerBusts;
    }

    public void incrementPlayerBusts() {
        this.totalPlayerBusts++;
    }

    public int getTotalHouseBusts() {
        return totalHouseBusts;
    }

    public void incrementHouseBusts() {
        this.totalHouseBusts++;
    }

    public int getTotalBankruptcies() {
        return totalBankruptcies;
    }

    public void incrementBankruptcies() { this.totalBankruptcies++; }
}
