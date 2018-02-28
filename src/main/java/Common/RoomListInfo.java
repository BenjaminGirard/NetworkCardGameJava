package Common;

import java.util.HashMap;

public class RoomListInfo {
    HashMap<Integer, RoomInfo> _rooms;

    public RoomListInfo() {
        _rooms = new HashMap<>();
    }

    public RoomListInfo fill(HashMap<Integer, RoomInfo> rooms) {
        _rooms = rooms;
        return this;
    }

    public void addRoom(RoomInfo room) {
        _rooms.put(room._id, room);

    }

    public HashMap<Integer, RoomInfo> getRooms() {
        return _rooms;
    }
}
