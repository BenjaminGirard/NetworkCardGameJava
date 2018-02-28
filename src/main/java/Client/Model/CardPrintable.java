package Client.Model;

import Common.Color;
import Common.Trump;

import java.util.HashMap;

public class CardPrintable {
    HashMap<Integer, String> _cardsNbr;
    HashMap<Color, String> _cardsColor;
    HashMap<Boolean, String> _cardIsPlayable;
    HashMap<Trump, String> _cardsTrump;

    public CardPrintable() {
        _cardsNbr = new HashMap<>();
        _cardsNbr.put(7, "7");
        _cardsNbr.put(8, "8");
        _cardsNbr.put(9, "9");
        _cardsNbr.put(10, "10");
        _cardsNbr.put(11, "J");
        _cardsNbr.put(12, "Q");
        _cardsNbr.put(13, "K");
        _cardsNbr.put(14, "A");
        _cardsColor = new HashMap<>();
        _cardsColor.put(Color.Heart, "H");
        _cardsColor.put(Color.Diamond, "D");
        _cardsColor.put(Color.Spade, "S");
        _cardsColor.put(Color.Club, "C");
        _cardIsPlayable = new HashMap<>();
        _cardIsPlayable.put(true, "p");
        _cardIsPlayable.put(true, " ");
        _cardsTrump = new HashMap<>();
        _cardsTrump.put(Trump.Heart, "Heart");
        _cardsTrump.put(Trump.Diamond, "Diamond");
        _cardsTrump.put(Trump.Spade, "Spade");
        _cardsTrump.put(Trump.Club, "Club");
        _cardsTrump.put(Trump.None, "No Trump");
        _cardsTrump.put(Trump.All, "All Trump");
    }

    public HashMap<Color, String> getCardsColor() {
        return _cardsColor;
    }

    public void setCardsColor(HashMap<Color, String> cardsColor) {
        _cardsColor = cardsColor;
    }

    public HashMap<Trump, String> getCardsTrump() {
        return _cardsTrump;
    }

    public HashMap<Integer, String> getcardsNbr() {
        return _cardsNbr;
    }

    public void setCardsNbr(HashMap cards) {
        _cardsNbr = cards;
    }

    public HashMap<Boolean, String> getCardIsPlayable() {
        return _cardIsPlayable;
    }
}
