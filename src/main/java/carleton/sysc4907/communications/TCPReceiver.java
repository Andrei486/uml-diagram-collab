package carleton.sysc4907.communications;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.time.LocalTime;

public class TCPReceiver implements Runnable {

    private final ObjectInputStream socketInput;
    private final MessageInterpreter interpreter;
    private long id;

    public TCPReceiver(Socket socket, MessageInterpreter interpreter, long id) throws IOException {
        this.socketInput = new ObjectInputStream(socket.getInputStream());
        this.interpreter = interpreter;
        this.id = id;
        System.out.println("Receiver Created");
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            try {
                Message message = (Message) socketInput.readObject();
                System.out.println("Message received at time " + LocalTime.now());
                interpreter.interpret(message, id);
            } catch (IOException | ClassNotFoundException e) {
                flag = false;
            }
        }
    }
}
