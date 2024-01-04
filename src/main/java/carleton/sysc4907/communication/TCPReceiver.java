package carleton.sysc4907.communication;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class TCPReceiver implements Runnable {

    private final ObjectInputStream socketInput;
    private final MessageInterpreter interpreter;

    public TCPReceiver(Socket socket, MessageInterpreter interpreter) throws IOException {
        this.socketInput = new ObjectInputStream(socket.getInputStream());
        this.interpreter = interpreter;
    }

    public void run() {
        boolean flag = true;
        while (flag) {
            try {
                Message message = (Message) socketInput.readObject();
                interpreter.interpret(message);
            } catch (IOException | ClassNotFoundException e) {
                flag = false;
            }
        }
    }
}
