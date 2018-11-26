package cards;

import android.os.Parcel;
import android.os.Parcelable;

/* Card class.  Holds FaceValue and Suit as Enums.  Value of cards can be obtained by calling
 * the getFaceValue method with the faceValue enum as parameter
 * (as is done in the BlackjackHand.evaluateCard method).  */
public class Card implements Parcelable {

    private Suit suit;
    private FaceValue faceValue;

    /* Constructor sets Enum fields. */
    public Card(Suit suit, FaceValue faceValue) {
        this.suit = suit;
        this.faceValue = faceValue;
    }

    public Card(Parcel in){
        this.suit = Suit.valueOf(in.readString());
        this.faceValue = FaceValue.valueOf(in.readString());
    }

    public int describeContents(){ return 0; }

    public void writeToParcel(Parcel out, int flags){
        out.writeString(this.suit.name());
        out.writeString(this.faceValue.name());
    }

    /*
   Parcelable implementation, includes CREATOR, describeContents, and WriteToParcel
    */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Card createFromParcel(Parcel in) {
            return new Card(in);
        }

        public Card[] newArray(int size) {
            return new Card[size];
        }
    };

    /* Methods to return Enum fields. */
    public Suit getSuit() { return this.suit; }

    public FaceValue getFaceValue() { return this.faceValue; }

}
