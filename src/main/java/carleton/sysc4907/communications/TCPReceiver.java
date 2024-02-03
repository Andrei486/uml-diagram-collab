package carleton.sysc4907.communications;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

/**
 * Receives messages from a client
 */
public class TCPReceiver implements Runnable {

    private final ObjectInputStream socketInput;
    private final MessageInterpreter interpreter;
    private long id;

    /**
     * constructs a TCPReceiver
     * @param socket the socket of the client
     * @param interpreter the MessageInterpreter of the system
     * @param id the id of the client
     * @throws IOException the port could not be set up
     */
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
                interpreter.interpret(message, id);
            } catch (IOException | ClassNotFoundException e) {
                flag = false;
            }
        }
    }
}
