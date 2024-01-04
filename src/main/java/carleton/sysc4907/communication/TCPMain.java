package carleton.sysc4907.communication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TCPMain {

    public static void main(String[] args) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("> Enter port:");
        TCPConnectionManager connectionManager = new TCPConnectionManager(Integer.parseInt(bufferedReader.readLine()));
        BlockingQueue<TargetedMessage> sendQueue = new LinkedBlockingQueue<>();
        TCPSender sender = new TCPSender(sendQueue, connectionManager);
        new Thread(connectionManager).start();
        new Thread(sender).start();
        new TCPMain().updateListeningPorts(bufferedReader, sender);
    }

    public void updateListeningPorts(BufferedReader userInput, TCPSender sender) throws IOException {
        System.out.println("> Enter hostname:port# to receive from:");
        String input = userInput.readLine();
        String[] inputValues = input.split(" ");
        for (String host : inputValues) {
            String[] address = host.split(":");
            Socket socket = null;
            try {
                socket = new Socket(address[0], Integer.parseInt(address[1]));
                var receiver = new TCPReceiver(socket, new MessageInterpreter());
                new Thread(receiver).start();
            } catch (Exception e) {
                e.printStackTrace();
                if (socket != null) {
                    socket.close();
                } else {
                    System.out.println("Invalid input.");
                }
            }
        }
        communicate(userInput, sender);
    }

    public void communicate(BufferedReader userInput, TCPSender sender) {
        try {
            System.out.println("> Connection established.");
            boolean flag = true;
            while (flag) {
                String toSend = userInput.readLine();
                if (toSend.equals("e")) {
                    flag = false;
                    break;
                } else if (toSend.equals("c")) {
                    updateListeningPorts(userInput, sender);
                } else {
                    var targetedMessage = new TargetedMessage(
                            new long[0],
                            true,
                            new Message(MessageType.UPDATE, toSend));
                    sender.enqueue(targetedMessage);
                }

            }
        } catch (Exception ignored) {
        }
    }
}
