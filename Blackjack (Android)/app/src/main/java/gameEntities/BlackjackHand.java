package gameEntities;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;
import java.util.ArrayList;
import cards.*;


public class BlackjackHand implements Parcelable {
    List<Card> theHand;

    public BlackjackHand() {
        this.theHand = new ArrayList<Card>();
    }

    public BlackjackHand(Parcel in) {
        in.readList(theHand, Card.class.getClassLoader());
    }

    public void addCardToHand(Card card) {
        this.theHand.add(card);
    }

    public int describeContents() { return 0; }

    public void writeToParcel(Parcel out, int flags){
        out.writeList(theHand);
    }

    /*
    Parcelable implementation, includes CREATOR, describeContents, and WriteToParcel
     */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public BlackjackHand createFromParcel(Parcel in) {
            return new BlackjackHand(in);
        }

        public BlackjackHand[] newArray(int size) {
            return new BlackjackHand[size];
        }
    };

    /* Returns the current value of the hand. */
    public int evaluateHand() {
        int handValue = 0;

        boolean acesInHand = false;

        /* First iteration that counts aces as 11.*/
        for (Card card : theHand) {
            handValue += evaluateCard(card);
            if (card.getFaceValue().equals(FaceValue.ACE))
                acesInHand = true;
        }

        /* If there are Aces in the hand, go through the hand and for each Ace,
         * if the total hand value is over 21, subtract 10 from the total hand value
         * (thus counting that Ace as 1).  Do this for each ace until the total
         * hand value is less than or equal to 21. */
        if (acesInHand) {
            for (Card card : theHand)
                if (card.getFaceValue().equals(FaceValue.ACE))
                    if (handValue > 21)
                        handValue -= 10;
        }

        return handValue;

    }

    /* Helper method used to link FaceValue enums and their integer values in the Blackjack
     * context.  Uses BlackjackValueMap class to link a cards Enum facevalue to an int value.  */
    private int evaluateCard(Card card) {
        return BlackjackValueMap.getValue(card.getFaceValue());
    }

    public List<Card> returnHand(){
        return theHand;
    }

}