package carleton.sysc4907.communications;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ClientData {
    private int id;
    private Socket socket;
    private ObjectOutputStream outputStream;
    private TCPReceiver receiver;

    public ClientData(int id, Socket socket) throws IOException {
        this.id = id;
        this.socket = socket;
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.receiver = new TCPReceiver(socket, new MessageInterpreter());

        this.receiver.run();
    }

    public ObjectOutputStream getOutputStream() {
        return outputStream;
    }
}
