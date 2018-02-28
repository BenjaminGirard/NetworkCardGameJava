package Server.Game;

import Common.*;
import Server.ServerConnexion.ClientInfos;
import Server.ServerConnexion.Room;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;


public class GameMasterCoinche {
    private static final char NB_CARDS_PER_COLOR = 15;
    private final ArrayList<Card> _cards;
    private final HashMap<ClientInfos, Card> _table;
    private final Random _shuffler = new Random();
    private GameInfos _gameInfos;


    public GameMasterCoinche() {
        _cards = new ArrayList<>();
        _table = new HashMap<>();
        _gameInfos = new GameInfos();
        generateCards();
    }

    public void generateCards() {
        AtomicInteger _cardsId = new AtomicInteger();
        for (Color color : Color.values()) {
            for (char i = 7; i < NB_CARDS_PER_COLOR; i++) {
                _cards.add(0, new Card().fill(_cardsId.getAndIncrement(),
                        i, color));
            }
        }
    }

    public void distribute(Room room) {
        ArrayList<ClientInfos> clients = room.getClients();
        Collections.shuffle(_cards);
        while (_cards.size() > 0)
            clients.forEach((ClientInfos c) -> {
                Card tmpCard = _cards.remove(0);
                c.addCard(tmpCard);
                room.getServer().sendToTCP(c.getId(), new SendCardToPlayer().fill(tmpCard));
                room.sendToRoom(new CardDistributed().fill(new Player().fill(c.getId(), c.getUserName())));
            });
    }

    public void shuffleCards() {

        int fi;
        int si;
        Card tmp;

        for (int i = 0; i < 100; i++) {
            fi = _shuffler.nextInt(_cards.size() - 1);
            si = _shuffler.nextInt(_cards.size() - 1);
            tmp = _cards.get(si);
            _cards.set(si, _cards.get(fi));
            _cards.set(fi, tmp);
        }

    }

    public void playCardOnTable(ClientInfos client, Card card) {
        _table.put(client, card);
    }

    public ClientInfos turnOver() {
        ClientInfos turnWinner = _gameInfos.updateScore(_table);
        putCardsInPack();
        return turnWinner;
    }

    public void putCardsInPack() {
        _table.forEach((client, card) -> {
            _cards.add(card);
        });
        _table.clear();
    }

    public void prepareNextRound() {
        _gameInfos.setTurn((_gameInfos.getTurn() + 1) % 4);
        _gameInfos.setCall(null);
        _gameInfos.setNbCalls(0);
        _table.clear();
        _cards.clear();
        generateCards();
    }

    public HashMap<ClientInfos, Card> getTable() {
        return _table;
    }

    public GameInfos getGameInfos() {
        return _gameInfos;
    }

    public void startGame(Room room) {
        distribute(room);
        room.getServer().sendToTCP(room.getClient((room.getGMCoinche().getGameInfos().getTurn() + 1) % 4).getId(), new YourTurnCall());
    }

    public void resetAll() {
        _gameInfos = new GameInfos();
        prepareNextRound();
    }

    public ArrayList<Card> getCards() {
        return _cards;
    }
}
