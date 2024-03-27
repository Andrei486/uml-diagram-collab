package carleton.sysc4907.communications;

import carleton.sysc4907.model.SessionModel;
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
    private SessionModel sessionModel;
    private MessageConstructor messageConstructor;

    /**
     * Constructs the object and starts the connection
     * @param ip ip of the host
     * @param port the port host application is connected to
     * @param clients the client list of the clientManager
     */
    ClientConnectionManager(String ip, int port, ClientList clients, MessageConstructor messageConstructor, SessionModel sessionModel) {
        this.clients = clients;
        this.messageConstructor = messageConstructor;
        this.sessionModel = sessionModel;
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
            messageConstructor.sendInvalid(new Message(MessageType.JOIN_REQUEST, sessionModel.getLocalUser().getUsername()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public int getPort() {
        return clientSocket.getPort();
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
