package Common;

public class RoomAccessAnswer {
    boolean _isAuthorized;

    public RoomAccessAnswer() {
    }

    public RoomAccessAnswer fill(boolean isAuthorized) {
        _isAuthorized = isAuthorized;
        return this;
    }

    public boolean getIsAuthorized() {
        return _isAuthorized;
    }
}
