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
    ClientConnectionManager(String ip, int port, ClientList clients) {
        this.clients = clients;
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
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Connection error");
            alert.setContentText("The application has encountered an error connecting to the host, please check that the information is correct and try again.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            alert.showAndWait();
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
