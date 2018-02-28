package Common;

public class AskUserAuth {
    String _userName;

    public AskUserAuth() {
    }

    public AskUserAuth fill(String userName) {
        _userName = userName;
        return this;
    }

    public String getUserName() {
        return _userName;
    }
}
