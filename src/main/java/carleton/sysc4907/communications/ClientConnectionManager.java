package carleton.sysc4907.communications;

import java.io.IOException;
import java.net.Socket;

/**
 * Makes a connection was a HostConnectionManager
 */
public class ClientConnectionManager {
    private Socket clientSocket;
    private ClientList clients;

    private MessageConstructor messageConstructor;

    /**
     * Constructs the object and starts the connection
     * @param ip ip of the host
     * @param port the port host application is connected to
     * @param clients the client list of the clientManager
     */
    ClientConnectionManager(String ip, int port, ClientList clients, MessageConstructor messageConstructor) {
        this.clients = clients;
        this.messageConstructor = messageConstructor;
        startConnection(ip, port);
    }

    /**
     * Connects to an available host
     * @param ip ip of the host
     * @param port the port host application is connected to
     */
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            System.out.println("Connection Made");
            clients.addClient(clientSocket);
            messageConstructor.sendInvalid(new Message(MessageType.JOIN_REQUEST, null));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
