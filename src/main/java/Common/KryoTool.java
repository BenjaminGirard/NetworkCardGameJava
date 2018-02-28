package Common;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Server;

public class KryoTool {
    public static void registerServerClasses(Server server) {
        register(server.getKryo());
    }

    public static void registerClientClass(Client client) {
        register(client.getKryo());
    }

    private static void register(Kryo kryo) {
        kryo.register(java.util.HashMap.class);

        kryo.register(AreYouReady.class);
        kryo.register(AskCreateRoom.class);
        kryo.register(AskUserAuth.class);
        kryo.register(AuthAnswer.class);
        kryo.register(Card.class);
        kryo.register(CardDistributed.class);
        kryo.register(CardPlayed.class);
        kryo.register(Color.class);
        kryo.register(GameCancelled.class);
        kryo.register(GameOverPoints.class);
        kryo.register(IAmReady.class);
        kryo.register(PartyCancelled.class);
        kryo.register(Player.class);
        kryo.register(PlayerCallResponse.class);
        kryo.register(PlayerReady.class);
        kryo.register(RoomAccessAnswer.class);
        kryo.register(RoomCreate.class);
        kryo.register(RoomDeleted.class);
        kryo.register(RoomInfo.class);
        kryo.register(RoomListInfo.class);
        kryo.register(RoomMovingClient.class);
        kryo.register(SendCardToPlayer.class);
        kryo.register(TeamAttribution.class);
        kryo.register(Trump.class);
        kryo.register(TurnOver.class);
        kryo.register(YourTurnCall.class);
        kryo.register(YourTurnPlayCard.class);
    }
}
