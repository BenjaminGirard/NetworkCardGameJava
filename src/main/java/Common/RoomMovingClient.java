package Common;

public class RoomMovingClient {
    Player _player;
    int _roomId;
    int _teamId;

    boolean _isJoining;

    public RoomMovingClient() {
    }

    public RoomMovingClient fill(Player player, int roomId, boolean isJoining, int teamId) {
        _player = player;
        _roomId = roomId;
        _isJoining = isJoining;
        _teamId = teamId;
        return this;
    }

    public Player getPlayer() {
        return _player;
    }

    public int getRoomId() {
        return _roomId;
    }

    public int getTeamId() {
        return _teamId;
    }

    public boolean getIsJoining() {
        return _isJoining;
    }
}
