package cards;

import java.util.EnumMap;

/* BlackjackValueMap is a Singleton class that links a Cards FaceValue Enum to its corresponding
 * int value according to Blackjack card valuations.  */
public class BlackjackValueMap {

    private final EnumMap<FaceValue, Integer> map = new EnumMap<FaceValue, Integer>(FaceValue.class);
    private static final BlackjackValueMap instance = new BlackjackValueMap();

    /* Private constructor, called above.  Populates EnumMap when called. */
    private BlackjackValueMap() {
        map.put(FaceValue.TWO, 2);
        map.put(FaceValue.THREE, 3);
        map.put(FaceValue.FOUR, 4);
        map.put(FaceValue.FIVE, 5);
        map.put(FaceValue.SIX, 6);
        map.put(FaceValue.SEVEN, 7);
        map.put(FaceValue.EIGHT, 8);
        map.put(FaceValue.NINE, 9);
        map.put(FaceValue.TEN, 10);
        map.put(FaceValue.JACK, 10);
        map.put(FaceValue.QUEEN, 10);
        map.put(FaceValue.KING, 10);
        map.put(FaceValue.ACE, 11);
        /* If card is an Ace, count it as 11 initially.  If Ace needs to be counted as 1,
         * this will be adjusted in the BlackjackHand evaluateHand method. */
    }

    /* Actual public method that gets called with the Cards Enum facevalue.  This method uses a private helper
     * method to access the EnumMap.*/
    public static int getValue(FaceValue faceValue) { return instance.getValueFromMap(faceValue); }

    /* Said private helper method. */
    private int getValueFromMap(FaceValue faceValue) { return map.get(faceValue); }

}

