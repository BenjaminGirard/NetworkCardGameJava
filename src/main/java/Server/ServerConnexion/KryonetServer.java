package Server.ServerConnexion;

import Common.KryoTool;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import static Common.Trump.*;

public class KryonetServer implements IServer {
    private final Server _server;
    private final ConcurrentHashMap<Integer, ClientInfos> _clients;
    private final ConcurrentHashMap<Integer, Room> _rooms;
    private final AtomicInteger _idGenRooms;
    private int _port = 4242;

    public KryonetServer(int port) {
        _server = new Server();
        _port = port;
        _clients = new ConcurrentHashMap<>();
        _rooms = new ConcurrentHashMap<>();
        _idGenRooms = new AtomicInteger();
    }

    @Override
    public void specifyPort(int port) {
        _port = port;
    }

    @Override
    public void startServer() {

        _server.start();

        try {
            _server.bind(_port);
        } catch (java.lang.Throwable e) {
            System.out.println("Cannot bind.\n" + e.toString());
            System.exit(84);
        }

        KryoTool.registerServerClasses(_server);

        _server.addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                synchronized (_clients) {
                    _clients.get(connection.getID()).addRequest(object);
                    _clients.notifyAll();
                }
            }

            @Override
            public void connected(Connection connection) {
                System.out.println("Player connected: " + connection.toString() + " -- id: " + connection.getID());
                _clients.put(connection.getID(), new ClientInfos(connection.getID()));
            }

            @Override
            public void disconnected(Connection connection) {
                System.out.println("Player disconnected: " + connection.getID());
                _clients.remove(connection.getID());
                _rooms.forEach((Integer key, Room room) -> {
                    room.tryRemoveClient(connection.getID(), _rooms);
                });
            }

        });
    }

    @Override
    public void disconnectServer() {
        _server.stop();
    }

    @Override
    public ConcurrentHashMap<Integer, ClientInfos> getClients() {
        return _clients;
    }

    @Override
    public void createRoom(ClientInfos client) {
        int tmpId = _idGenRooms.getAndIncrement();
        _rooms.put(tmpId, new Room(tmpId, client, _server, _clients));
    }

    public Server getServer() {
        return _server;
    }

    public Room getRoom(int roomId) {
        return _rooms.get(roomId);
    }

    public ConcurrentHashMap<Integer, Room> getRooms() {
        return _rooms;
    }

    public ClientInfos getClient(int id) {

        return _clients.get(id);
    }

    public void debugServer() throws IOException {
        HashMap fromTrumpToString = new HashMap<Object, Object>() {{
            put(Heart, "Heart");
            put(Diamond, "Diamond");
            put(Club, "Club");
            put(Spade, "Spade");
            put(All, "All");
            put(None, "None");
        }};


        System.out.println("\n\n\n\n---- CLIENT LIST ----");
        _clients.forEach((k, c) -> {
            System.out.println("- id=[" + c.getId() + "] - userName=[" + c.getUserName() + "] - RoomId=[" + c.getRoom() + "] -");
        });
        System.out.println("-- END CLIENT LIST --");

        System.out.println("\n----------ROOM LIST------------\n");
        _rooms.forEach((k, r) -> {
            System.out.println("----- ROOM id={" + r.getId() + "}");
            System.out.println("----GAME INFOS----");
            System.out.println("| Cards in pack:\t" + r.getGMCoinche().getCards().size() + " |");
            System.out.println("| Cards on table:\t" + r.getGMCoinche().getTable().size() + " |");
            System.out.println("| Score:\tTeamPair={" + r.getGMCoinche().getGameInfos().getScoreTPair() + "} - TeamImpair={" +
                    r.getGMCoinche().getGameInfos().getScoreTImpair() + "} |");
            System.out.println("| Trump:\t" + r.getGMCoinche().getGameInfos().getTrump() + " |");
            System.out.println("------------------");

            r.getClients().forEach((c) -> {
                System.out.println("- id=[" + c.getId() + "] - userName=[" + c.getUserName() + "] - TeamId=["
                        + c.getTeamId() + "] - isReady=" + String.valueOf(c.getIsReady()) + " - nbCards=[" + c.getCards().size() + "]");
            });
            System.out.println("---End room info---");
        });
        System.out.println("\n----------END ROOM LIST--------\n");

    }

    boolean isUserNameValid(String userName) {
        if (userName == null || userName.length() == 0)
            return false;
        boolean[] isTaken = {true};
        _clients.forEach((id, client) -> {
            if (client.getUserName() != null && client.getUserName().compareTo(userName) == 0)
                isTaken[0] = false;
        });
        return isTaken[0];
    }
}
