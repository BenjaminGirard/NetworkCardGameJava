package Common;

public class CardDistributed {
    Player _player;

    public CardDistributed() {
    }

    public CardDistributed fill(Player player) {
        _player = player;
        return this;
    }

    public Player getPlayer() {
        return _player;
    }
}
