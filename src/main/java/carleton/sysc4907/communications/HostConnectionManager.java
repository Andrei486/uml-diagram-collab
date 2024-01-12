package carleton.sysc4907.communications;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class HostConnectionManager implements Runnable{
    private ServerSocket serverSocket;
    private HostManager hostManager;

    public HostConnectionManager(int port, HostManager hostManager) throws IOException {
        this.hostManager = hostManager;
        this.serverSocket = new ServerSocket(port);
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                hostManager.addClient(socket);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
