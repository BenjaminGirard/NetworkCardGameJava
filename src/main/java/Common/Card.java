package Common;

public class Card {
    int _id;
    int _nbr;
    Color _color;
    boolean _isPlayable;

    public Card() {
    }

    public Card fill(int id, int nbr, Color color) {
        _id = id;
        _nbr = nbr;
        _color = color;
        _isPlayable = true;
        return this;
    }

    public int getNbr() {
        return _nbr;
    }


    public Color getColor() {
        return _color;
    }


    public boolean getIsPlayable() {
        return _isPlayable;
    }

    public void setIsPlayable(boolean isPlayable) {
        _isPlayable = isPlayable;
    }

    public int getId() {
        return _id;
    }
}
