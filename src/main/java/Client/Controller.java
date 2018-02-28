package Client;

import Client.Model.CardPrintable;
import Client.Model.InstanceModel;
import Client.Model.ReadyState;
import Client.Model.RoomListModel;
import Client.NetWork.ClientConnection;
import Common.*;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import java.io.Console;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;

import static java.lang.Thread.sleep;

public class Controller {
    Player _player;
    boolean _isInRoom;
    ClientConnection _cl;
    RoomListModel _rlm;
    InstanceModel _im;
    CardPrintable _cp;

    public Controller(String ipAdress, int port) throws IOException {
        _cl = new ClientConnection(5000, ipAdress, port);
        _rlm = new RoomListModel();
        _im = new InstanceModel(
        );
        _player = new Player();
        _isInRoom = false;
        _cp = new CardPrintable();
        KryoTool.registerClientClass(_cl.getClient());

    }

    public ClientConnection getCl() {
        return _cl;
    }

    public void setCl(ClientConnection cl) {
        _cl = cl;
    }

    public RoomListModel getRlm() {
        return _rlm;
    }

    public void setRlm(RoomListModel rlm) {
        _rlm = rlm;
    }

    public InstanceModel getIm() {
        return _im;
    }

    public void setIm(InstanceModel im) {
        _im = im;
    }

    public void askCreateRoom() {
        AskCreateRoom ask = new AskCreateRoom();
        _cl.sendRequest(ask);
    }

    public void askUserAuth() {
        //View qui demande username
        System.out.println("Username ? :\r");
        Scanner scan = new Scanner(System.in);
        String line = scan.nextLine();
        AskUserAuth userAuth = new AskUserAuth();
        userAuth.fill(line);
        _cl.sendRequest(userAuth);
    }

    public void askLeaveRoom() {
        RoomMovingClient moove = new RoomMovingClient();
        if (!_isInRoom) {
            System.out.println("You are nor in a room..");
            return;
        }
        moove.fill(_player,
                _im.getRoom().getId(),
                false,
                _im.getTeam());
        _cl.sendRequest(moove);
    }

    public void showRoomList() {
        if (_isInRoom)
            System.out.println("You are not in the lobby.");
        else
            _rlm.getRooms().forEach((key, value) -> {
                System.out.println("\n\n\n================");
                System.out.println("RoomId[" + value.getId() + "] (" + value.getPlayers().size() + "/4 players)");
                System.out.println("________________");
                value.getPlayers().forEach((k, v) -> {
                    System.out.println(v.getUserName());
                });
                System.out.println("=================");
            });
        if (_rlm.getRooms().size() == 0)
            System.out.println("There is no room..");
    }


    public void tryJoinRoom() {
        if (_isInRoom) {
            System.out.println("You are not in the lobby.");
            return;
        }
        if (_rlm.getRooms().size() < 1) {
            System.out.println("There is no room available. Use \"createroom\" to create a new room.");
            return;
        }
        boolean goodId = false;
        while (!goodId) {
            showRoomList();
            System.out.println("Which room would you like to join ? Enter the RoomId :");
            Scanner scan = new Scanner(System.in);
            String line = scan.nextLine();
            try {
                if (line == "cancel")
                    return;
                int id = Integer.parseInt(line);
                if (!_rlm.getRooms().containsKey(id)) {
                    System.out.println("This room doesn't exist...");
                    continue;
                }
                if (_rlm.getRooms().get(id).getPlayers().size() == 4) {
                    System.out.println("There is already four players...");
                    continue;
                }
                goodId = true;
                RoomMovingClient room = new RoomMovingClient();
                room.fill(_player, _rlm.getRooms().get(id).getId(), true, 0);
                _cl.sendRequest(room);
            } catch (Exception ex) {
                System.out.println("Please enter an integer or \"cancel\"");
            }
        }
    }

    public void sendIAmReady() {
        if (!_isInRoom) {
            System.out.println("You are not in a room");
        }
        if (_im.getReadyState() == ReadyState.CLICKABLE) {
            _cl.sendRequest(new IAmReady().fill(_player));
        } else
            System.out.println("You can't do that");
    }

    public void showHandCards() {
        if (!_isInRoom) {
            System.out.println("You are not in a room");
        }
        _im.getHandCards().forEach((k, v) -> {
            System.out.println(k + ": |" + _cp.getcardsNbr().get(v.getNbr()) + _cp.getCardsColor().get(v.getColor()) + "|");
        });
    }

    public void passAnnounce() {
        PlayerCallResponse response = new PlayerCallResponse();
        response.setIsCalling(false);
        response.setPlayer(_player);
        _cl.sendRequest(response);
    }

    public void sendCallResponse(Trump trump, int call) {
        PlayerCallResponse response = new PlayerCallResponse();
        response.fill(_player, true, trump, call);
        _cl.sendRequest(response);
        return;
    }

    public void trumpAnnounce(int call) {
        boolean isOk = false;
        if (_im.getCall().getTrump() == Trump.All) {
            PlayerCallResponse response = new PlayerCallResponse();
            response.setPlayer(_player);
            response.setIsCalling(false);
            _cl.sendRequest(response);
            return;
        }
        while (!isOk) {
            System.out.println("Choose your trump ? (c/d/h/s/n/a)");
            Trump trump;
            Scanner scan = new Scanner(System.in);
            String line = scan.nextLine();
            try {
                switch (line) {
                    case "c":
                        trump = Trump.Club;
                        break;
                    case "d":
                        trump = Trump.Diamond;
                        break;
                    case "h":
                        trump = Trump.Heart;
                        break;
                    case "s":
                        trump = Trump.Spade;
                        break;
                    case "n":
                        trump = Trump.None;
                        break;
                    case "a":
                        trump = Trump.All;
                        break;
                    default:
                        trump = null;
                        break;
                }
                if (trump == null)
                    continue;
                if (_im.getCall().getTrump() == null) {
                    sendCallResponse(trump, call);
                    return;
                }
                if (trump.ordinal() < _im.getCall().getTrump().ordinal()) {
                    System.out.println("Please enter a higher trump");
                    continue;
                }
                isOk = true;
                sendCallResponse(trump, call);
            } catch (Exception ex) {
                System.out.println("Please enter a trump (c/d/h/s/n/a)");
            }
        }
    }

    public void callAnnounce() {
        boolean isOk = false;
        while (!isOk) {
            System.out.println("Choose your call ? (" + _im.getCall().getCall() + " - 500)");
            Scanner scan = new Scanner(System.in);
            String line = scan.nextLine();
            try {
                int call = Integer.parseInt(line);

                if (call < _im.getCall().getCall() || call > 500) {
                    System.out.println("You call must be between " + _im.getCall().getCall() + " and 500");
                    continue;
                }
                isOk = true;
                trumpAnnounce(call);
            } catch (Exception ex) {
                System.out.println("Please enter a number");
            }
        }
    }

    public void tryCall() {
        boolean isOk = false;
        if (!_isInRoom) {
            System.out.println("You are not in a room");
            return;
        }
        if (!_im.getIsMyTurnCall()) {
            System.out.println("This is not your turn to call");
        } else {
            while (!isOk) {
                System.out.println("Do you want to call ? (y/n)");
                Scanner scan = new Scanner(System.in);
                String line = scan.nextLine();
                if (line == null)
                    continue;
                if (line.equals("n")) {
                    passAnnounce();
                    isOk = true;
                } else if (line.equals("y")) {
                    isOk = true;
                    callAnnounce();
                } else
                    System.out.println("[" + line + "]");
            }
        }
        _im.setIsMyTurnCall(false);
    }

    public void helper() {
        System.out.println("==================================\n" +
                "              HELPER            \n" +
                "==================================\n" +
                "\"help\" -> Helper\n" +
                "\"call\" -> Call." +
                "\"createroom\" -> Create a room.\n" +
                "\"disconnect\" -> Disconnect.\n" +
                "\"join\" -> Join a room.\n" +
                "\"leave\" -> Leave room.\n" +
                "\"ready\" -> I am ready.\n" +
                "\"roomlist\" -> Show room list.\n" +
                "\"showmycards\" -> Show my cards.\n");

    }

    public void disconnect() {
        _cl.setConnected(false);
    }

    public void fillCommands(HashMap<String, Runnable> commands) {
        commands.put("help", () -> helper());
        commands.put("createroom", () -> askCreateRoom());
        commands.put("leave", () -> askLeaveRoom());
        commands.put("disconnect", () -> disconnect());
        commands.put("roomlist", () -> showRoomList());
        commands.put("join", () -> tryJoinRoom());
        commands.put("ready", () -> sendIAmReady());
        commands.put("showmycards", () -> showHandCards());
        commands.put("call", () -> tryCall());
    }

    public void iAmMovingRoom(RoomMovingClient moveInfo, RoomInfo modifiedRoom) {
        if (moveInfo.getIsJoining()) {
            System.out.println("Room joined !");
            RoomInfo room = _rlm.getRooms().get(moveInfo.getRoomId());
            _isInRoom = true;
            _im = new InstanceModel(_player, room);
            room.getPlayers().forEach((key, value) -> {
                _im.addPlayer(value.getId());
            });
            modifiedRoom.addPlayer(moveInfo.getPlayer());
            _im.setTeam(moveInfo.getTeamId());
        } else {
            System.out.println("Room left !");
            _im = new InstanceModel();
            _isInRoom = false;
        }
        modifiedRoom.removePlayer(moveInfo.getPlayer());
    }

    public void aPlayerIsMoving(RoomMovingClient moveInfo, RoomInfo modifiedRoom) {
        if (moveInfo.getIsJoining()) {
            if (_isInRoom) {
                if ((_im.getRoom().getId() == moveInfo.getRoomId())) {
                    System.out.println(moveInfo.getPlayer().getUserName() + " joigned the room !");
                    _im.addPlayer(moveInfo.getPlayer().getId());
                }
            }
            modifiedRoom.addPlayer(moveInfo.getPlayer());
        } else {

            if (_isInRoom) {
                if ((_im.getRoom().getId() == moveInfo.getRoomId()))
                    System.out.println(moveInfo.getPlayer().getUserName() + " left the room...");
                _im.removePlayer(moveInfo.getPlayer().getId());
            }
            modifiedRoom.removePlayer(moveInfo.getPlayer());
        }
    }

    public void printTableCards() {
        _im.getTableCards().forEach((k, c) -> {
            System.out.print("|" + _cp.getcardsNbr().get(c.getNbr()) + _cp.getCardsColor().get(c.getColor()) + "|  ");
        });
    }

    public void showCardToChoose() {
        System.out.println("\n\n\n\n");
        printTableCards();
        System.out.println("\n\n");
        BinaryTreeRules treeRules = new BinaryTreeRules();
        _im.getHandCards().forEach((Integer k, Card c) -> {
            if (_im.getTableCards().size() != 0) {
                c.setIsPlayable(treeRules.isMyCardPlayable(_im, c));
            } else
                c.setIsPlayable(true);
            System.out.println("ID(" + c.getId() + ") -- |" + _cp.getcardsNbr().get(c.getNbr()) +
                    _cp.getCardsColor().get(c.getColor()) + "| " + _cp.getCardIsPlayable().get(c.getIsPlayable()));
        });


        System.out.println("What card do you want to play ? Enter card id.");
    }


    public void chooseCardToPlay() {
        boolean isOk = false;

        System.out.println("It's your turn to play !");
        _im.setIsMyTurn(true);
        showCardToChoose();
        Console console = System.console();
        while (!isOk) {
            String line = console.readLine();
            try {
                int id = Integer.parseInt(line);
                if (_im.getHandCards().containsKey(id)) {
                    if (_im.getHandCards().get(id).getIsPlayable()) {
                        CardPlayed cardPlayed = new CardPlayed();
                        cardPlayed.fill(_player, _im.getHandCards().get(id));
                        _im.setIsMyTurn(false);
                        _cl.sendRequest(cardPlayed);
                        return;
                    }
                    System.out.println("Please enter an card id");
                }
            } catch (Exception ex) {
                System.out.println("Please enter a number");
            }
        }
    }

    public void cardPlayed(CardPlayed cardPlayed) {
        System.out.println(cardPlayed.getPlayer().getUserName() + " played |"
                + _cp.getcardsNbr().get(cardPlayed.getCard().getNbr())
                + _cp.getCardsColor().get(cardPlayed.getCard().getColor()) + "| !");
        _im.cardPlayed(cardPlayed.getPlayer(), cardPlayed.getCard());
        printTableCards();
    }

    public void start() throws InterruptedException {
        _player.setId(_cl.getClient().getID());
        _cl.getClient().addListener(new Listener() {
            @Override
            public void received(Connection connection, Object object) {
                if (object instanceof AreYouReady)
                    _im.setReadyClickable();
                else if (object instanceof IAmReady)
                    _im.setReadyUnclickable(((IAmReady) object).getPlayer());
                else if (object instanceof GameCancelled)
                    _im.clearAllCards();
                else if (object instanceof PartyCancelled)
                    _im.resetInstance();
                else if (object instanceof RoomDeleted)
                    _rlm.removeRoom(((RoomDeleted) object).getId());
                else if (object instanceof TurnOver)
                    _im.turnOver((TurnOver) object);
                else if (object instanceof TeamAttribution)
                    _im.teamAttribution((TeamAttribution) object);
                else if (object instanceof RoomCreate)
                    _rlm.addRoom(((RoomCreate) object).getRoom());
                else if (object instanceof CardPlayed)
                    _im.cardPlayed(((CardPlayed) object).getPlayer(), ((CardPlayed) object).getCard());
                else if (object instanceof PlayerReady)
                    _im.setReadyUnclickable(((PlayerReady) object).getPlayer());
                else if (object instanceof SendCardToPlayer)
                    _im.addHandCards(((SendCardToPlayer) object).getCard());
                else if (object instanceof CardPlayed) {
                    cardPlayed((CardPlayed) object);
                } else if (object instanceof YourTurnCall) {
                    System.out.println("It's your turn to call !");
                    _im.setIsMyTurnCall(true);
                } else if (object instanceof YourTurnPlayCard)
                    chooseCardToPlay();
                else if (object instanceof PlayerCallResponse) {
                    _im.announceCall((PlayerCallResponse) object);
                } else if (object instanceof AuthAnswer) {
                    if (((AuthAnswer) object).getIsAuthorized()) {
                        _player = new Player().fill(_cl.getClient().getID(), ((AuthAnswer) object).getUserName());
                        System.out.println("Hello " + _player.getUserName() + " ! =)");
                        System.out.println("Press \"help\" to see the commands.");

                    } else
                        System.out.println("Bad user name or already used.. Please choose an other one ! =)");
                } else if (object instanceof CardDistributed) {
                    if (((CardDistributed) object).getPlayer().getId() != _player.getId()) {
                        _im.otherCardDistibuted(((CardDistributed) object).getPlayer().getId());
                    }
                } else if (object instanceof RoomAccessAnswer) {
                    RoomAccessAnswer answer = (RoomAccessAnswer) object;
                    if (answer.getIsAuthorized()) {
                        System.out.println("Joining...");
                    } else {
                        System.out.println("Denied...");
                    }
                } else if (object instanceof RoomListInfo) {
                    _rlm.setRooms(((RoomListInfo) object).getRooms());
                    showRoomList();
                } else if (object instanceof RoomMovingClient) {
                    RoomMovingClient moveInfo = (RoomMovingClient) object;
                    RoomInfo modifiedRoom = _rlm.getRooms().get(moveInfo.getRoomId());
                    if (moveInfo.getPlayer().getId() == _player.getId())
                        iAmMovingRoom(moveInfo, modifiedRoom);
                    else
                        aPlayerIsMoving(moveInfo, modifiedRoom);
                    _rlm.getRooms().put(moveInfo.getRoomId(), modifiedRoom);
                }
                // faire un refresh de a view
            }

        });
        HashMap<String, Runnable> commands = new HashMap<String, Runnable>();
        fillCommands(commands);
        while (_cl.isConnected()) {
            while (_player.getUserName() == null) {
                askUserAuth();
                sleep(1000);
            }
            Scanner scan = new Scanner(System.in);
            String line = scan.nextLine();
            if (!commands.containsKey(line)) {
                System.out.println("Commands not found.. Enter \"help\" for help !");

            } else
                commands.get(line).run();

        }
        System.out.println("UserName : " + _player.getUserName() + "\nId : " + _player.getId() + "\n");
        System.out.println("Not connected..\n");
    }
}
