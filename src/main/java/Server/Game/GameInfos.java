package Server.Game;

import Common.Card;
import Common.Color;
import Common.PlayerCallResponse;
import Common.Trump;
import Server.ServerConnexion.ClientInfos;

import java.util.HashMap;

public class GameInfos {
    private final CardsValues _cardsValues;
    private final int _partyPointsPair;
    private final int _partyPointsImpair;
    private int _scoreTPair;
    private int _scoreTImpair;
    private PlayerCallResponse _call;
    private int _nbCalls;
    private int _turn;
    private Color _colorPlayed;

    public GameInfos() {
        _scoreTPair = 0;
        _scoreTImpair = 0;
        _cardsValues = new CardsValues();
        _call = null;
        _colorPlayed = Color.Heart;
        _turn = 0;
        _nbCalls = 0;
        _partyPointsImpair = 0;
        _partyPointsPair = 0;
    }


    public boolean compareCards(Card trueCard, Card falseCard) {
        if (falseCard == null)
            return true;
        if (isTrump(trueCard) && isTrump(falseCard) == false)
            return true;
        if (isTrump(trueCard) == false && isTrump(falseCard))
            return false;
        if (_call.getTrump() != Trump.None && _call.getTrump() != Trump.All && trueCard.getColor() == _colorPlayed && falseCard.getColor() != _colorPlayed)
            return true;
        if (_call.getTrump() != Trump.None && _call.getTrump() != Trump.All && trueCard.getColor() != _colorPlayed && falseCard.getColor() == _colorPlayed)
            return false;
        if (_cardsValues.getCardValue(trueCard, isTrump(trueCard)) > _cardsValues.getCardValue(falseCard, isTrump(falseCard)))
            return true;
        return false;
    }

    public ClientInfos updateScore(HashMap<ClientInfos, Card> tableCards) {
        ClientInfos[] clientWinning = {null};
        int[] tmpScore = {0};

        tableCards.forEach((client, card) -> {
            if (compareCards(card, tableCards.get(clientWinning[0])) == true)
                clientWinning[0] = client;
            tmpScore[0] = tmpScore[0] + _cardsValues.getCardValue(card, isTrump(card));
        });
        if (clientWinning[0].getTeamId() == 0)
            _scoreTPair += tmpScore[0];
        else
            _scoreTImpair += tmpScore[0];
        return clientWinning[0];
    }

    public boolean isTrump(Card card) {
        if (_call.getTrump() == Trump.None)
            return false;
        if (_call.getTrump() == Trump.All)
            return true;
        return card.getColor().ordinal() == _call.getTrump().ordinal();
    }

    public Trump getTrump() {

        return _call == null ? null : _call.getTrump();
    }

    public Color getColorPlayed() {
        return _colorPlayed;
    }

    public void setColorPlayed(Color _colorPlayed) {
        this._colorPlayed = _colorPlayed;
    }

    public int getScoreTPair() {
        return _scoreTPair;
    }

    public void setScoreTPair(int _scoreTPair) {
        this._scoreTPair = _scoreTPair;
    }


    public int getScoreTImpair() {
        return _scoreTImpair;
    }

    public void setScoreTImpair(int _scoreTImpair) {
        this._scoreTImpair = _scoreTImpair;
    }


    public CardsValues getCardsValues() {
        return _cardsValues;
    }

    public int getTurn() {
        return _turn;
    }

    public void setTurn(int turn) {
        _turn = turn;
    }

    public int getNbCalls() {
        return _nbCalls;
    }

    public void setNbCalls(int nbCalls) {
        _nbCalls = nbCalls;
    }

    public PlayerCallResponse getCall() {
        return _call;
    }

    public void setCall(PlayerCallResponse call) {
        _call = call;
    }

    public int getPartyPointsPair() {
        return _partyPointsPair;
    }

    public int getPartyPointsImpair() {
        return _partyPointsImpair;
    }
}




