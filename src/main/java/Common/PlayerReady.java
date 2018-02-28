package Common;

public class PlayerReady {
    Player _player;

    public PlayerReady() {
    }

    public PlayerReady fill(Player player) {
        _player = player;
        return this;
    }

    public Player getPlayer() {
        return _player;
    }
}
