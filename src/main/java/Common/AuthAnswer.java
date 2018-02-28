package Common;

public class AuthAnswer {
    boolean _isAuthorized;

    String _userName;

    public AuthAnswer() {
    }

    public AuthAnswer fill(boolean isAuthorized, String userName) {

        _isAuthorized = isAuthorized;
        _userName = userName;
        return this;
    }

    public boolean getIsAuthorized() {
        return _isAuthorized;
    }

    public String getUserName() {
        return _userName;
    }
}
