package Client.NetWork;

import com.esotericsoftware.kryonet.Client;

import java.io.IOException;

public class ClientConnection {
    Client _client;
    int _timeout;
    String _IpAddress;
    int _port;
    boolean _isConnected;

    public ClientConnection(int timeout, String ipAddress, int port) throws IOException {
        _client = new Client();
        _timeout = timeout;
        _IpAddress = ipAddress;
        _port = port;
        _client.start();
        _client.connect(timeout, ipAddress, port);
        _isConnected = _client.isConnected();
    }

    public boolean isConnected() {
        return _isConnected;
    }

    public void setConnected(boolean connected) {
        _isConnected = connected;
    }

    public int getTimeout() {
        return _timeout;
    }

    public void setTimeout(int timeout) {
        _timeout = timeout;
    }

    public String getIpAddress() {
        return _IpAddress;
    }

    public void setIpAddress(String IpAddress) {
        _IpAddress = IpAddress;
    }

    public int getPort() {
        return _port;
    }

    public void setPort(int port) {
        _port = port;
    }

    public Client getClient() {
        return _client;
    }

    public void setClient(Client client) {
        _client = client;
    }

    public void connectClient() throws IOException {
        _client.connect(_timeout, _IpAddress, _port);
    }

    public void sendRequest(Object obj) {
        _client.sendTCP(obj);
    }
}
