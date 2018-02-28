package Client.Model;

import Common.RoomInfo;

import java.util.HashMap;

public class RoomListModel extends AModel {
    HashMap<Integer, RoomInfo> _rooms;

    public RoomListModel() {
        _rooms = new HashMap<Integer, RoomInfo>();
    }

    public HashMap<Integer, RoomInfo> getRooms() {
        return _rooms;
    }

    public void setRooms(HashMap<Integer, RoomInfo> rooms) {
        _rooms = rooms;
    }

    public void addRoom(RoomInfo room) {
        _rooms.put(room.getId(), room);
    }

    public void removeRoom(int id) {
        _rooms.remove(id);
    }
}
