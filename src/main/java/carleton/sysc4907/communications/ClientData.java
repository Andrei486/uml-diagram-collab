package carleton.sysc4907.communications;

import carleton.sysc4907.command.AddCommandFactory;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientData {
    private long id;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private TCPReceiver receiver;
    private Boolean isValid;

    public ClientData(long id, Socket socket, MessageInterpreter messageInterpreter) throws IOException {
        this.id = id;
        this.isValid = false;
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.receiver = new TCPReceiver(socket, messageInterpreter);

        new Thread(this.receiver).start();
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }

    public long getId() {
        return id;
    }

    public Boolean getValid() {
        return isValid;
    }
}