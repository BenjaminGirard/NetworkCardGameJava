package Common;

public class IAmReady {
    Player _player;

    public IAmReady() {
    }

    public IAmReady fill(Player player) {
        _player = player;
        return this;
    }

    public Player getPlayer() {
        return _player;
    }
}
