package Client;

import Client.Model.InstanceModel;
import Common.Card;
import Common.Trump;

public class BinaryTreeRules {
    InstanceModel _im;
    Card _card;


    public BinaryTreeRules() {
    }

    public boolean isMyCardPlayable(InstanceModel im, Card card) {
        _im = im;
        _card = card;
        return isAnyCardPlayed();
    }

    private boolean isAnyCardPlayed() {
        return _im.getTableCards().size() > 0 ? isColorPlayed() : true;
    }

    private boolean isColorPlayed() {
        if (_im.getFirstCardPlayed().getNbr() == 0)
            return true;
        else
            return isTrump(_im.getFirstCardPlayed()) == true ? isMyCardTrump() : isMyCardColor();
    }


    private boolean isMyCardTrump() {
        return isTrump(_card) == true ? isMyCardHigher() : doIHaveTrump();
    }

    private boolean doIHaveTrump() {
        boolean[] iHaveTrump = {false};

        _im.getHandCards().forEach((k, c) -> {
            if (isTrump(c) == true)
                iHaveTrump[0] = true;
        });
        return iHaveTrump[0] ? false : true;
    }

    private boolean isMyCardHigher() {
        boolean[] isTableHigher = {false};
        Card[] higherCard = {_card};

        _im.getTableCards().forEach((k, c) -> {
            if (isTrump(c) && c.getNbr() > _card.getNbr()) {
                isTableHigher[0] = true;
                if (higherCard[0].getNbr() < c.getNbr())
                    higherCard[0] = c;
            }
        });
        return isTableHigher[0] ? doIHaveHigher(higherCard[0]) : true;
    }

    private boolean doIHaveHigher(Card higherCard) {
        boolean[] iHaveHigher = {false};

        _im.getHandCards().forEach((k, c) -> {
            if (isTrump(c) && c.getNbr() > higherCard.getNbr())
                iHaveHigher[0] = true;
        });
        return iHaveHigher[0] ? false : true;
    }

    private boolean isMyCardColor() {
        return _card.getColor() == _im.getFirstCardPlayed().getColor() ? true : doIHaveColor();
    }

    private boolean doIHaveColor() {
        boolean[] iHaveColor = {false};
        _im.getHandCards().forEach((k, c) -> {
            if (c.getColor() == _im.getFirstCardPlayed().getColor())
                iHaveColor[0] = true;
        });
        return iHaveColor[0] ? false : isMyCardTrump();
    }


    public boolean isTrump(Card card) {
        if (_im.getCall().getTrump() == Trump.None)
            return false;
        if (_im.getCall().getTrump() == Trump.All)
            return true;
        
        return card.getColor().ordinal()
                == _im.getCall().getTrump().ordinal();
    }
}
