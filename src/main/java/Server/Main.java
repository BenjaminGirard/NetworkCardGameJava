package Server;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws InterruptedException, IOException {
        if (args.length != 1) {
            System.out.println("please specify a port:\njava -jar jcoinche-server.jar [port]");
            return;
        }
        int port;
        try {
            port = Integer.parseInt(args[0]);
        } catch (Exception e) {
            System.out.println("please specify a port:\njava -jar jcoinche-server.jar [port]");
            return;
        }
        Core core = new Core(port);
        core.run();
    }

}
