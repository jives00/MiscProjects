package cards;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Collections;
//import java.util.List;
import java.util.Stack;

/* Deck class that generates and deals Cards.  Uses Java's Stack to achieve LIFO functionality. */
public class Deck implements Parcelable{

    Stack<Card> deck;

    public Deck() {
        this.deck = new Stack<Card>();
        makeDeck();
    }

    public Deck(Parcel in){
        in.readList(deck,Stack.class.getClassLoader());
    }

    public int describeContents(){ return 0; }

    public void writeToParcel(Parcel out, int flags){
        out.writeList(deck);
    }

    /*
   Parcelable implementation, includes CREATOR, describeContents, and WriteToParcel
    */
    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Deck createFromParcel(Parcel in) {
            return new Deck(in);
        }

        public Deck[] newArray(int size) { return new Deck[size];
        }
    };

    /* Adds standard 52 playing cards to the Deck classes Stack.
     * NOTE:  This does NOT automatically shuffle the deck.  That
     * must be done after creation through the shuffle method. */
    private void makeDeck() {
        for (Suit s : Suit.values()) {
            for (FaceValue fv : FaceValue.values()) {
                deck.add(new Card(s, fv));
            }
        }
    }

    // Uses the Collections.shuffle method to randomize the order of the cards in the deck stack.
    public void shuffle() {
        Collections.shuffle(this.deck);
    }

    // Removes and returns the top card using the Stacks pop method.
    public Card dealCard() {
        return deck.pop();
    }

}
