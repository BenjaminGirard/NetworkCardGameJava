package Server.Game;

import Common.Card;

import java.util.HashMap;

public class CardsValues {
    private final HashMap<Integer, HashMap<Boolean, Integer>> CARDS_VALUES;

    public CardsValues() {
        CARDS_VALUES = new HashMap<Integer, HashMap<Boolean, Integer>>() {
            {
                put(7, new HashMap<Boolean, Integer>() {
                    {
                        put(true, 0);
                        put(false, 0);
                    }
                });
                put(8, new HashMap<Boolean, Integer>() {
                    {
                        put(true, 0);
                        put(false, 0);
                    }
                });
                put(9, new HashMap<Boolean, Integer>() {
                    {
                        put(true, 14);
                        put(false, 0);
                    }
                });
                put(10, new HashMap<Boolean, Integer>() {
                    {
                        put(true, 10);
                        put(false, 10);
                    }
                });
                put(11, new HashMap<Boolean, Integer>() {
                    {
                        put(true, 20);
                        put(false, 2);
                    }
                });
                put(12, new HashMap<Boolean, Integer>() {
                    {
                        put(true, 3);
                        put(false, 3);
                    }
                });
                put(13, new HashMap<Boolean, Integer>() {
                    {
                        put(true, 4);
                        put(false, 4);
                    }
                });
                put(14, new HashMap<Boolean, Integer>() {
                    {
                        put(true, 11);
                        put(false, 11);
                    }
                });
            }
        };
    }

    public int getCardValue(Card card, boolean isTrump) {
        return CARDS_VALUES.get(card.getNbr()).get(isTrump);
    }
}
