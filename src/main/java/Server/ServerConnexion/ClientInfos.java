package Server.ServerConnexion;

import Common.Card;

import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class ClientInfos {
    private final int _id;
    private final ConcurrentLinkedQueue<Object> _requests;
    private final HashMap<Integer, Card> _cards;
    private String _userName;
    private int _roomId;
    private int _teamId;
    private boolean _isReady;


    public ClientInfos(int id) {
        _id = id;
        _requests = new ConcurrentLinkedQueue<Object>();
        _roomId = 0;
        _teamId = 0;
        _isReady = false;
        _cards = new HashMap<>();
    }

    public void addRequest(Object request) {
        _requests.add(request);
    }

    public Object pollRequest() {
        return _requests.poll();
    }

    public boolean isRequestEmpty() {
        return _requests.isEmpty();
    }

    public int getId() {
        return _id;
    }

    public String getUserName() {
        return _userName;
    }

    public void setUserName(String userName) {
        _userName = userName;
    }

    public int getRoom() {
        return _roomId;
    }

    public int getTeamId() {
        return _teamId;
    }

    public void setTeamId(int teamId) {
        _teamId = teamId;
    }

    public boolean getIsReady() {
        return _isReady;
    }

    public void setIsReady(boolean isReady) {
        _isReady = isReady;
    }

    public void setRoomId(int room) {
        _roomId = room;
    }

    public HashMap<Integer, Card> getCards() {
        return _cards;
    }

    public Card getCard(int id) {
        return _cards.get(id);

    }

    public void addCard(Card card) {
        _cards.put(card.getId(), card);
    }

    public Card dropCard(int id) {
        return _cards.remove(id);
    }

    public void resetStatus() {
        _roomId = 0;
        _teamId = 0;
        _isReady = false;
        _cards.clear();
    }
}



