package Client.Model;

public class Score {
    int _scoreA;
    int _scoreB;


    public Score(int a, int b) {
        _scoreA = a;
        _scoreB = b;
    }

    public Score() {
        _scoreA = 0;
        _scoreB = 0;

    }

    public int get_scoreA() {
        return _scoreA;
    }

    public void set_scoreA(int scoreA) {
        _scoreA = scoreA;
    }

    public int get_scoreB() {
        return _scoreB;
    }

    public void set_scoreB(int scoreB) {
        _scoreB = scoreB;
    }
}