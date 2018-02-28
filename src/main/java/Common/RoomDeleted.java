package Common;

public class RoomDeleted {
    int _id;

    public RoomDeleted() {

    }

    public RoomDeleted fill(int id) {
        _id = id;
        return this;
    }

    public int getId() {
        return _id;
    }
}
