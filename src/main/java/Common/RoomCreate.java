package Common;

public class RoomCreate {
    RoomInfo _room;

    public RoomCreate() {
    }

    public RoomCreate fill(RoomInfo room) {
        _room = room;
        return this;
    }

    public RoomInfo getRoom() {
        return _room;
    }
}
