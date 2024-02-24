package carleton.sysc4907.communications;

import javafx.scene.control.Alert;
import javafx.scene.layout.Region;

import java.io.IOException;
import java.net.Socket;

/**
 * Makes a connection was a HostConnectionManager
 */
public class ClientConnectionManager {
    private Socket clientSocket;
    private ClientList clients;

    /**
     * Constructs the object and starts the connection
     * @param ip ip of the host
     * @param port the port host application is connected to
     * @param clients the client list of the clientManager
     */
    ClientConnectionManager(String ip, int port, ClientList clients) throws IOException {
        this.clients = clients;
        startConnection(ip, port);
    }

    /**
     * Connects to an available host
     * @param ip ip of the host
     * @param port the port host application is connected to
     */
    public void startConnection(String ip, int port) throws IOException {
        clientSocket = new Socket(ip, port);
        System.out.println("Connection Made");
        clients.addClient(clientSocket);
    }

    /**
     * closes this objects socket (maybe has no purpose)
     * @throws IOException socket fails to close
     */
    public void stopConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
