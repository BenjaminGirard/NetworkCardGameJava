package Common;

import java.util.HashMap;
import java.util.Map;

public class RoomInfo {
    int _id;
    HashMap<Integer, Player> _players;

    public RoomInfo() {
    }

    public RoomInfo fill(int id) {
        _id = id;
        _players = new HashMap<>();
        return this;
    }


    public void addPlayer(Player player) {
        _players.put(player._id, player);
    }

    public void removePlayer(Player player) {
        _players.remove(player._id);
    }

    public int getId() {
        return _id;
    }

    public Map<Integer, Player> getPlayers() {
        return _players;
    }
}
