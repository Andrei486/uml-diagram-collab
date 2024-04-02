package carleton.sysc4907.communications;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * Manages and creates connections with incoming clients
 */
public class HostConnectionManager implements Runnable{
    private ServerSocket serverSocket;
    private HostManager hostManager;
    private ClientList clients;

    /**
     * Constructs the object and starts the connection
     * @param clients the client list of the host
     * @param hostManager the host the connection belongs to
     * @throws IOException the connection failed
     */
    public HostConnectionManager(ClientList clients, HostManager hostManager) throws IOException {
        this.hostManager = hostManager;
        this.serverSocket = new ServerSocket(0);
        this.hostManager.setPort(serverSocket.getLocalPort());
        this.clients = clients;
    }

    /**
     * Adds a client connection to the client list
     * @param socket the socket of the client
     */
    public void addClient(Socket socket) {
        try {
            clients.addClient(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * closes this objects socket
     * @throws IOException socket fails to close
     */
    public void close() throws IOException {
        serverSocket.close();
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }


    /**
     * Loop that accepts connects and adds them to the client list
     */
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
