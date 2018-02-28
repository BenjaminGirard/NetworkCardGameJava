package Client;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length != 2) {
            System.out.println("Usage : Jcoinche-client.jar [Ip Adress] [Port]");
            return;
        }
        try {
            int port = Integer.parseInt(args[1]);
            Controller ctr = new Controller(args[0], port);
            ctr.start();
        } catch (Exception ex) {
            System.out.println("Port must be a number...");
            return;
        }
    }
}
