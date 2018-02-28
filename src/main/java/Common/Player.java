package Common;

public class Player {
    int _id;
    String _userName;

    public Player() {
    }

    public Player fill(int id, String userName) {
        _id = id;
        _userName = userName;
        return this;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        _userName = userName;
    }
}
