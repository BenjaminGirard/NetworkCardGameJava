package Server.ServerConnexion;

import java.util.concurrent.ConcurrentHashMap;

public interface IServer {
    public void specifyPort(int port);

    public void startServer();

    public void disconnectServer();

    public ConcurrentHashMap<Integer, ClientInfos> getClients();

    public void createRoom(ClientInfos client);
}
