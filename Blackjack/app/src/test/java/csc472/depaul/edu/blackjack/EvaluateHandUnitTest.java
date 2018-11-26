package csc472.depaul.edu.blackjack;

import org.junit.Test;

import java.nio.file.FileAlreadyExistsException;

import cards.Card;

import cards.FaceValue;

import cards.Suit;

import gameEntities.BlackjackHand;

import static org.junit.Assert.assertEquals;

public class EvaluateHandUnitTest {

    @Test
    public void evaluateHandTest(){

        BlackjackHand hand = new BlackjackHand();

        hand.addCardToHand(new Card(Suit.CLUBS, FaceValue.ACE));

        assertEquals(11,hand.evaluateHand());

        hand.addCardToHand(new Card(Suit.DIAMONDS,FaceValue.FIVE));

        assertEquals(16,hand.evaluateHand());

        hand.addCardToHand(new Card(Suit.HEARTS,FaceValue.QUEEN));

        assertEquals(16,hand.evaluateHand());

        hand.addCardToHand(new Card(Suit.SPADES, FaceValue.ACE));

        assertEquals(17,hand.evaluateHand());

    }
}
