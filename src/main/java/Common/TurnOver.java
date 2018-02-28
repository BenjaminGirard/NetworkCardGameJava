package Common;

public class TurnOver {
    int _teamWinner;
    int _scoreTeamPair;
    int _scoreTeamImpair;

    public TurnOver() {
    }

    public TurnOver fill(int teamWinner, int scoreTeamPair, int scoreTeamImpair) {
        _teamWinner = teamWinner;
        _scoreTeamPair = scoreTeamPair;
        _scoreTeamImpair = scoreTeamImpair;
        return this;
    }

    public int getTeamWinner() {
        return _teamWinner;
    }

    public int getScoreTeamPair() {
        return _scoreTeamPair;
    }

    public int getScoreTeamImpair() {
        return _scoreTeamImpair;
    }
}
