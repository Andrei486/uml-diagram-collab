package carleton.sysc4907.communications;

import carleton.sysc4907.communications.messages.JoinMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Client extends Thread{
    private Socket clientSocket;
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;
    private String username;

    public Client(String username) {
        this.username = username;
    }
    public void startConnection(String ip, int port) {
        try {
            clientSocket = new Socket(ip, port);
            inputStream = new ObjectInputStream(clientSocket.getInputStream());
            outputStream = new ObjectOutputStream(clientSocket.getOutputStream());
            sendJoinMessage();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void stopConnection() {
        try {
            clientSocket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendJoinMessage() {
        try {
            outputStream.writeObject(new JoinMessage(MessageType.JOIN, username));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public void run() {
        startConnection("localhost", 4000);
        stopConnection();
    }

    public static void main(String[] args) {
        new Client("c1").start();
    }
}
