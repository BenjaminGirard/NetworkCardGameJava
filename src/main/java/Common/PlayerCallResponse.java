package Common;

public class PlayerCallResponse {
    Player _player;
    private boolean _isCalling;
    private Trump _trump;
    private int _call;

    public PlayerCallResponse() {

    }

    public PlayerCallResponse fill(Player player, boolean isCalling, Trump trump, int call) {
        _player = player;
        _isCalling = isCalling;
        _trump = trump;
        _call = call;
        return this;
    }

    public boolean getIsCalling() {
        return _isCalling;
    }

    public void setIsCalling(boolean _isCalling) {
        this._isCalling = _isCalling;
    }

    public Trump getTrump() {
        return _trump;
    }

    public void setTrump(Trump _trump) {
        this._trump = _trump;
    }

    public int getCall() {
        return _call;
    }

    public void setCall(int _call) {
        this._call = _call;
    }

    public Player getPlayer() {
        return _player;
    }

    public void setPlayer(Player _player) {
        this._player = _player;
    }
}
