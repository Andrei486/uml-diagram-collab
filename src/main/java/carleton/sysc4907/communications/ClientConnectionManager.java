package carleton.sysc4907.communications;

import java.io.IOException;
import java.net.Socket;

public class ClientConnectionManager {
    private Socket clientSocket;
    private ClientList clients;

    ClientConnectionManager(String ip, int port, ClientList clients) {
        this.clients = clients;
        startConnection(ip, port);
    }


    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            System.out.println("Connection Made");
            clients.addClient(clientSocket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void stopConnection() {
        try {
            clientSocket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
