package Server.ServerConnexion;

import Common.*;
import Server.Game.GameMasterCoinche;
import com.esotericsoftware.kryonet.Server;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class Room {
    private final int _id;
    private final Server _server;
    private final ConcurrentHashMap<Integer, ClientInfos> _allClients;
    private final ArrayList<ClientInfos> _clients = new ArrayList<>();
    private final GameMasterCoinche _GMCoinche;

    public Room(int id, ClientInfos client, Server server, ConcurrentHashMap<Integer, ClientInfos> clients) {
        _server = server;
        _allClients = clients;
        _id = id;
        _GMCoinche = new GameMasterCoinche();
        sendAllMessage(new RoomCreate().fill(getRoomInfos()));
        addClient(client);
    }

    public void attributeTeam() {
        int[] team = {1};
        TeamAttribution teams = new TeamAttribution();
        _clients.forEach(
                (ClientInfos c) -> {
                    c.setTeamId(team[0] % 2);
                    teams.addToTeams(new Player().fill(c.getId(), c.getUserName()), team[0] % 2);
                    team[0] = team[0] + 1;
                });
        sendToRoom(teams);
    }

    public void addClient(ClientInfos client) {
        _clients.add(client);
        // add restrictions (password for room access, etc.)
        client.setRoomId(_id);
        _server.sendToTCP(client.getId(), new RoomAccessAnswer().fill(true));
        sendAllMessage(new RoomMovingClient().fill(new Player().fill(client.getId(), client.getUserName()), _id, true, client.getTeamId()));
        if (_clients.size() == 4) {
            attributeTeam();
            sendToRoom(new AreYouReady());
        }
    }

    public boolean tryRemoveClient(Integer id, ConcurrentHashMap<Integer, Room> roomList) {
        ClientInfos tmpClient = getClientFromId(id);
        if (tmpClient == null)
            return false;
        _clients.remove(_clients.indexOf(tmpClient));
        tmpClient.resetStatus();
        sendAllMessage(new RoomMovingClient().fill((new Player().fill(tmpClient.getId(), tmpClient.getUserName())), _id, false, tmpClient.getTeamId()));
        if (_clients.size() == 0) {
            roomList.remove(_id);
            sendAllMessage(new RoomDeleted().fill(_id));
        } else {
            cancelParty();
        }
        return (true);
    }

    public ArrayList<ClientInfos> getClients() {
        return _clients;
    }

    public ClientInfos getClient(int idx) {
        return _clients.get(idx);
    }

    public int howManyClients() {
        return _clients.size();
    }

    public void sendAllMessage(Object o) {
        _allClients.forEach((Integer k, ClientInfos v) -> {
            _server.sendToTCP(k, o);
        });
    }

    public void sendToRoom(Object o) {
        _clients.forEach((ClientInfos c) -> {
            _server.sendToTCP(c.getId(), o);
        });
    }

    public int getId() {
        return _id;
    }

    public RoomInfo getRoomInfos() {
        RoomInfo infos = new RoomInfo().fill(_id);

        _clients.forEach((v) -> {
            infos.addPlayer(new Player().fill(v.getId(), v.getUserName()));
        });
        return infos;
    }

    public Server getServer() {
        return _server;
    }

    public boolean isEveryoneReady() {
        boolean[] res = {true};
        _clients.forEach((c) -> {
            if (c.getIsReady() == false)
                res[0] = false;
        });
        return res[0];
    }

    public ClientInfos getClientFromId(int id) {
        for (ClientInfos client : _clients) {
            if (client.getId() == id)
                return client;
        }
        return null;
    }

    public void redistribute() {
        sendToRoom(new GameCancelled());
        _GMCoinche.prepareNextRound();
        _clients.forEach(c -> {
            c.getCards().clear();
        });
        _GMCoinche.startGame(this);

    }

    public GameMasterCoinche getGMCoinche() {
        return _GMCoinche;
    }

    private void resetClientsStateGame() {
        _clients.forEach(c -> {
            c.getCards().clear();
            c.setIsReady(false);
        });
    }

    private void cancelParty() {
        if (_clients.size() == 3)
            sendToRoom(new PartyCancelled());
        resetClientsStateGame();
        _GMCoinche.resetAll();
    }

    void updateGameOver() {
        int teamNb = getClientFromId(_GMCoinche.getGameInfos().getCall().getPlayer().getId()).getTeamId();
        int scoreTeamCalling = teamNb == 0 ? _GMCoinche.getGameInfos().getScoreTPair() : _GMCoinche.getGameInfos().getScoreTImpair();
        if (_GMCoinche.getGameInfos().getCall().getCall() <= scoreTeamCalling) {
            if (teamNb == 0)
                _GMCoinche.getGameInfos().setScoreTPair(_GMCoinche.getGameInfos().getPartyPointsPair() + _GMCoinche.getGameInfos().getCall().getCall());
            else
                _GMCoinche.getGameInfos().setScoreTImpair(_GMCoinche.getGameInfos().getPartyPointsImpair() + _GMCoinche.getGameInfos().getCall().getCall());
        } else {
            if (teamNb == 0)
                _GMCoinche.getGameInfos().setScoreTImpair(_GMCoinche.getGameInfos().getPartyPointsImpair() + _GMCoinche.getGameInfos().getCall().getCall());
            else
                _GMCoinche.getGameInfos().setScoreTPair(_GMCoinche.getGameInfos().getPartyPointsPair() + _GMCoinche.getGameInfos().getCall().getCall());

        }
        _GMCoinche.getGameInfos().setScoreTPair(0);
        _GMCoinche.getGameInfos().setScoreTImpair(0);

        _GMCoinche.prepareNextRound();
        _GMCoinche.distribute(this);
    }
}

