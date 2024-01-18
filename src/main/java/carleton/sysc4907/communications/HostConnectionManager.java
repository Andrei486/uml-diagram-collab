package carleton.sysc4907.communications;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

public class HostConnectionManager implements Runnable{
    private ServerSocket serverSocket;
    private HostManager hostManager;
    private ClientList clients;

    public HostConnectionManager(int port, ClientList clients, HostManager hostManager) throws IOException {
        this.hostManager = hostManager;
        this.serverSocket = new ServerSocket(port);
        this.clients = clients;
    }

    public void addClient(Socket socket) {
        try {
            clients.addClient(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() throws IOException {
        serverSocket.close();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("Connection Made");
                addClient(socket);
            }
        } catch (IOException e) {
            // Will occur when closing the server socket
            System.out.println("Server socket closed");
            return;
        }
    }
}
