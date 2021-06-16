package customer;

import java.io.IOException;
import java.net.ServerSocket;

public class RandomPort {

    private static RandomPort INSTANCE;

    private int port;

    private RandomPort() {
        try (ServerSocket serverSocket = new ServerSocket(0)) {
            this.port = serverSocket.getLocalPort();
            System.setProperty("RANDOM_PORT", String.valueOf(this.port));
        }
        catch (IOException ignored) {
        }
    }

    public static RandomPort getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RandomPort();
        }
        return INSTANCE;
    }

    public int getPort() {
        return this.port;
    }
}
