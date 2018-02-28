package Client.Model;

public class Other {
    int _id;
    int _nbCards;
    ReadyState _isReady;

    public Other(int id) {
        _id = id;
        _nbCards = 0;
        _isReady = ReadyState.INVISIBLE;
    }

    public int getId() {
        return _id;
    }

    public void setId(int id) {
        _id = id;
    }

    public int getNbCards() {
        return _nbCards;
    }

    public void setNbCards(int nbCards) {
        _nbCards = nbCards;
    }

    public ReadyState isReady() {
        return _isReady;
    }

    public void setReady(ReadyState ready) {
        _isReady = ready;
    }
}
