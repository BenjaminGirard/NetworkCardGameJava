package Server;

import Server.ServerConnexion.ClientInfos;
import Server.ServerConnexion.KryonetServer;
import Server.ServerConnexion.RequestHandler;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class Core {
    KryonetServer _server;
    RequestHandler _requestHandler;

    public Core(int port) {
        _server = new KryonetServer(port);
        _requestHandler = new RequestHandler(_server);
    }

    public void run() throws InterruptedException, IOException {
        _server.startServer();

        while (true) {
            ConcurrentHashMap<Integer, ClientInfos> _tmpClients = _server.getClients();
            synchronized (_tmpClients) {
                _tmpClients.wait();
                _tmpClients.forEach((Integer key, ClientInfos client) -> {
                    while (client.isRequestEmpty() == false) {
                        _requestHandler.handleRequest(client, client.pollRequest());
                    }
                });
            }
            _server.debugServer();
        }
    }
}
