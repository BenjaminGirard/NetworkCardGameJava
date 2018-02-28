package Server.ServerConnexion;

import Common.*;

public class RequestHandler {
    KryonetServer _server;

    public RequestHandler(KryonetServer server) {
        _server = server;
    }

    public void handleRequest(ClientInfos client, Object o) {
        System.out.println("handling request from client {" + client.getId() + "} for object : " + o.toString());
        if (o instanceof AskCreateRoom)
            handleAskCreateRoom(client, (AskCreateRoom) o);
        else if (o instanceof AskUserAuth)
            handleAskUserAuth(client, (AskUserAuth) o);
        else if (o instanceof CardPlayed)
            handleCardPlayed(client, (CardPlayed) o);
        else if (o instanceof IAmReady)
            handleIAmReady(client, (IAmReady) o);
        else if (o instanceof PlayerCallResponse)
            handlePlayerCallResponse(client, (PlayerCallResponse) o);
        else if (o instanceof PlayerReady)
            handlePlayerReady(client, (PlayerReady) o);
        else if (o instanceof RoomMovingClient)
            handleRoomMovingClient(client, (RoomMovingClient) o);
    }

    private void handleAskCreateRoom(ClientInfos client, AskCreateRoom o) {
        _server.createRoom(client);
    }

    private void handleAskUserAuth(ClientInfos client, AskUserAuth o) {
        boolean _userValid = _server.isUserNameValid(o.getUserName());

        if (_userValid) {
            client.setUserName(o.getUserName());
            _server.getServer().sendToTCP(client.getId(), new AuthAnswer().fill(true, client.getUserName()));
            RoomListInfo roomsInfo = new RoomListInfo();
            _server.getRooms().forEach((k, v) -> {
                roomsInfo.addRoom(v.getRoomInfos());
            });
            _server.getServer().sendToTCP(client.getId(), roomsInfo);
        } else
            _server.getServer().sendToTCP(client.getId(), new AuthAnswer().fill(false, client.getUserName()));

    }

    public void handleCardPlayed(ClientInfos client, CardPlayed o) {
        Card cardPlayed = o.getCard();
        Room currentRoom = _server.getRoom(client.getRoom());

        currentRoom.sendToRoom(o);
        currentRoom.getGMCoinche().playCardOnTable(client, client.dropCard(o.getCard().getId()));
        if (currentRoom.getGMCoinche().getTable().size() == 4) {
            handleTurnOver(currentRoom);
        } else {
            _server.getServer().sendToTCP(currentRoom.getClients().get((currentRoom.getClients().indexOf(client) + 1) % 4).getId(), new YourTurnPlayCard());
        }
    }

    public void handleIAmReady(ClientInfos client, IAmReady o) {
        Room currentRoom = _server.getRoom(client.getRoom());
        currentRoom.sendToRoom(o);
        client.setIsReady(true);
        if (currentRoom.isEveryoneReady()) {
            currentRoom.getGMCoinche().startGame(currentRoom);
        }
    }

    public void handlePlayerCallResponse(ClientInfos client, PlayerCallResponse o) {
        Room currentRoom = _server.getRoom(client.getRoom());
        int currentNbCalls = currentRoom.getGMCoinche().getGameInfos().getNbCalls();

        currentRoom.sendToRoom(o);
        if (o.getIsCalling() == false) {
            currentRoom.getGMCoinche().getGameInfos().setNbCalls(currentNbCalls + 1);
            if (currentRoom.getGMCoinche().getGameInfos().getNbCalls() == 4)
                if (currentRoom.getGMCoinche().getGameInfos().getCall() == null)
                    currentRoom.redistribute();
                else
                    _server.getServer().sendToTCP(currentRoom.getClient((currentRoom.getGMCoinche().getGameInfos().getTurn() + 1) % 4).getId(), new YourTurnPlayCard());
            else
                _server.getServer().sendToTCP(currentRoom.getClient((currentRoom.getClients().indexOf(client) + 1) % 4).getId(), new YourTurnCall());
        } else {
            currentRoom.getGMCoinche().getGameInfos().setNbCalls(1);
            currentRoom.getGMCoinche().getGameInfos().setCall(o);
            _server.getServer().sendToTCP(currentRoom.getClient((currentRoom.getClients().indexOf(client) + 1) % 4).getId(), new YourTurnCall());
        }
    }

    private void handlePlayerReady(ClientInfos client, PlayerReady o) {
        client.setIsReady(true);
        _server.getRoom(client.getRoom()).sendToRoom(o);
    }


    private void handleRoomMovingClient(ClientInfos client, RoomMovingClient o) {
        if (o.getIsJoining())
            _server.getRoom(o.getRoomId()).addClient(_server.getClient(client.getId()));
        else
            _server.getRoom(o.getRoomId()).tryRemoveClient(client.getId(), _server.getRooms());
    }


    private void handleTurnOver(Room currentRoom) {
        ClientInfos winner = currentRoom.getGMCoinche().turnOver();

        currentRoom.sendToRoom(new TurnOver().fill(winner.getTeamId(), currentRoom.getGMCoinche().getGameInfos().getScoreTPair(),
                currentRoom.getGMCoinche().getGameInfos().getScoreTImpair()));

        if (winner.getCards().size() == 0) {
            currentRoom.updateGameOver();
            currentRoom.sendToRoom(new GameOverPoints().fill(currentRoom.getGMCoinche().getGameInfos().getPartyPointsPair(),
                    currentRoom.getGMCoinche().getGameInfos().getPartyPointsImpair()));
            _server.getServer().sendToTCP(currentRoom.getClient((currentRoom.getGMCoinche().getGameInfos().getTurn() + 1) % 4).getId(), new YourTurnCall());
        } else {
            _server.getServer().sendToTCP(winner.getId(), new YourTurnPlayCard());
        }
    }


}
