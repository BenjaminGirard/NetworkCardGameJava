package Common;

public class CardPlayed {
    Player _player;
    Card _card;

    public CardPlayed() {
    }

    public void fill(Player player, Card card) {
        _player = player;
        _card = card;
    }

    public Player getPlayer() {
        return _player;
    }

    public void setPlayer(Player player) {
        _player = player;
    }

    public Card getCard() {
        return _card;
    }

    public void setCard(Card card) {
        _card = card;
    }
}
