package carleton.sysc4907.communications;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Host extends Thread{

    ServerSocket serverSocket;
    ArrayList<ClientHandler> clients;

    public void start(int port) {
        try {
            serverSocket = new ServerSocket(port);
            clients = new ArrayList<>();
            System.out.println("Host Started: Waiting For Clients");

            while (true) {
                System.out.println("Connection Checking");
                Socket socket = serverSocket.accept();
                System.out.println("Connection Made");
                createClient(socket);

            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createClient(Socket socket) {
        System.out.println("Step 1");
        ClientHandler clientHandler = new ClientHandler(socket);
        System.out.println("Step 2");
        clients.add(clientHandler);
        System.out.println("Step 3");
        clientHandler.start();
        System.out.println("Step 4");
    }


    public void run() {
        start(4000);
    }
}
