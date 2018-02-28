package Client.Model;

import Common.*;

import java.util.HashMap;
import java.util.Map;

import static Client.Model.ReadyState.UNCLICKABLE;

public class InstanceModel extends AModel {
    Player _player;
    RoomInfo _room;
    PlayerCallResponse _call;
    int _team;
    boolean _isMyTurnPlay;
    boolean _isMyTurnCall;
    Score _score;
    ReadyState _readyState;
    HashMap<Integer, Card> _handCards;
    HashMap<Integer, Card> _tableCards;
    HashMap<Integer, Other> _others;

    Card _firstCardPlayed;

    public InstanceModel(Player player, RoomInfo room) {
        _player = player;
        _handCards = new HashMap<>();
        _tableCards = new HashMap<>();
        _others = new HashMap<>();
        _isMyTurnPlay = false;
        _isMyTurnCall = false;
        _score = new Score(0, 0);
        _readyState = ReadyState.INVISIBLE;
        _room = room;
        _call = new PlayerCallResponse().fill(null, false, null, 0);
        _firstCardPlayed = new Card();
        _team = -1;

    }

    public InstanceModel(PlayerCallResponse call, HashMap<Integer, Card> handCards, HashMap<Integer, Card> tableCards, Card first) {
        _handCards = handCards;
        _tableCards = tableCards;
        _call = call;
        _firstCardPlayed = first;

    }

    public InstanceModel() {
    }

    public PlayerCallResponse getCall() {
        return _call;
    }

    public void setCall(PlayerCallResponse call) {
        _call = call;
    }

    public boolean getIsMyTurnCall() {
        return _isMyTurnCall;
    }

    public void setIsMyTurnCall(boolean isMyTurnCall) {
        _isMyTurnCall = isMyTurnCall;
    }

    public ReadyState getReadyState() {
        return _readyState;
    }

    public void setReadyState(ReadyState state) {
        _readyState = state;
    }

    public int getTeam() {
        return _team;
    }

    public void setTeam(int team) {
        _team = team;
    }

    public RoomInfo getRoom() {
        return _room;
    }

    public void setRoom(RoomInfo room) {
        _room = room;
    }

    public HashMap<Integer, Card> getHandCards() {
        return _handCards;
    }

    public void addHandCards(Card card) {
        _handCards.put(card.getId(), card);
    }

    public void removeHandCard(Card card) {
        _handCards.remove(card.getId());
    }

    public HashMap<Integer, Card> getTableCards() {
        return _tableCards;
    }

    public void addTableCards(Card card) {
        _tableCards.put(card.getId(), card);
    }

    public boolean isMyTurn() {
        return _isMyTurnPlay;
    }

    public void setIsMyTurn(boolean isMyTurnPlay) {
        _isMyTurnPlay = isMyTurnPlay;
    }

    public Score getScore() {
        return _score;
    }

    public void setScore(Score score) {
        _score = score;
    }

    public void addPlayer(int id) {
        _others.put(id, new Other(id));
    }

    public void removePlayer(int id) {
        _others.remove(id);
    }

    public void otherCardDistibuted(int id) {
        Other other = _others.get(id);
        other._nbCards++;
        _others.put(id, other);
    }

    public void othersCardPlayed(int id, Card card) {
        Other other = _others.get(id);
        other._nbCards--;
        _others.put(id, other);
        addTableCards(card);
    }

    public void playerCardPlayed(Card card) {
        removeHandCard(card);
        addTableCards(card);
    }

    public void setOtherReadyState(Player player, ReadyState state) {
        System.out.println(player.getUserName() + " is ready !");
        Other other = _others.get(player.getId());
        other._isReady = state;
        _others.put(player.getId(), other);
    }

    public void setOthersReadyState(ReadyState state) {
        for (Map.Entry<Integer, Other> entry : _others.entrySet()) {
            Other other = entry.getValue();
            other._isReady = state;
            _others.put(entry.getKey(), other);
        }
    }

    public void cardPlayed(Player player, Card card) {

        if (_firstCardPlayed.getNbr() == 0)
            _firstCardPlayed = card;
        if (player.getId() == _player.getId())
            playerCardPlayed(card);
        else
            othersCardPlayed(player.getId(), card);

    }

    public void setReadyClickable() {
        System.out.println("Are you ready ? Enter ready !");
        setReadyState(ReadyState.CLICKABLE);
        setOthersReadyState(ReadyState.CLICKABLE);
    }

    public void setReadyUnclickable(Player player) {
        if (_player.getId() == player.getId())
            setReadyState(UNCLICKABLE);
        else
            setOtherReadyState(player, ReadyState.UNCLICKABLE);
    }

    public void setReadyInvisible() {
        setReadyState(ReadyState.INVISIBLE);
        setOthersReadyState(ReadyState.INVISIBLE);
    }

    public void announceCall(PlayerCallResponse call) {
        CardPrintable cp = new CardPrintable();
        if (!call.getIsCalling())
            System.out.println(call.getPlayer().getUserName() + " passed !");
        else {
            _call.fill(call.getPlayer(), call.getIsCalling(), call.getTrump(), call.getCall());
            System.out.println(call.getPlayer().getUserName() + " called " + call.getCall() + " " + cp.getCardsTrump().get(call.getTrump()));
        }
    }

    public Card getFirstCardPlayed() {
        return _firstCardPlayed;
    }

    public void setOtherCardToZero() {
        _others.forEach((k, o) -> {
            o._nbCards = 0;
        });
    }

    public void clearAllCards() {
        _handCards.clear();
        _firstCardPlayed = new Card();
        setOtherCardToZero();
    }

    public void resetInstance() {
        clearAllCards();
        _isMyTurnPlay = false;
        _isMyTurnCall = false;
        _score = new Score(0, 0);
        _readyState = ReadyState.INVISIBLE;
        _call = new PlayerCallResponse().fill(null, false, Trump.None, 0);
        _firstCardPlayed = new Card();
        _team = -1;
    }

    public void turnOver(TurnOver turnOver) {
        System.out.println("Team " + turnOver.getTeamWinner() + " won this turn..");
        _score.set_scoreA(turnOver.getScoreTeamPair());
        _score.set_scoreB(turnOver.getScoreTeamImpair());
        System.out.println("=============================");
        System.out.println("Team A : " + _score.get_scoreA() + "   || Team B: " + _score.get_scoreB() + "    ");
        System.out.println("=============================");
        _tableCards.clear();
        _firstCardPlayed = new Card();
    }

    public void teamAttribution(TeamAttribution teamAttribution) {
        teamAttribution._teams.forEach((p, t) -> {
            System.out.println("===========================");
            System.out.println(p.getUserName() + " is in team " + t + " !");
        });
        _team = teamAttribution.getTeamFromId(_player.getId());
    }
}

