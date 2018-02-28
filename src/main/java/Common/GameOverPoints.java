package Common;

public class GameOverPoints {
    public int _partyPointsPair;
    public int _partyPointsImpair;

    public GameOverPoints() {
    }

    public GameOverPoints fill(int pair, int impair) {
        _partyPointsPair = pair;
        _partyPointsImpair = impair;
        return this;
    }

    public int getPartyPointsPair() {
        return _partyPointsPair;
    }

    public int getPartyPointsImpair() {
        return _partyPointsImpair;
    }
}
