package carleton.sysc4907.communications;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class ClientHandler extends Thread{
    private Socket clientSocket;
    private String username;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public ClientHandler(Socket socket) {
        try {
            System.out.println(" Step 1.1");
            this.clientSocket = socket;
            System.out.println(" Step 1.2");
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            System.out.println(" Step 1.3");
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            System.out.println(" Step 1.4");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void close() {
        try {
            clientSocket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void run() {
        Object message;
        try {
            while((message = inputStream.readObject()) != null) {
                if (message instanceof JoinMessage) {
                    System.out.println("User " + ((JoinMessage) message).getUsername() + " has joined the server");
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }
}
