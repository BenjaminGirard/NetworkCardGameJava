package Common;

public class SendCardToPlayer {
    Card _card;

    public SendCardToPlayer() {
    }

    public SendCardToPlayer fill(Card card) {
        _card = card;
        return this;
    }

    public Card getCard() {
        return _card;
    }
}
