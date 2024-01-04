package carleton.sysc4907.communication;

import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * A class that handles sending TCP messages to one other user.
 */
public class TCPSenderThread extends Thread {
    private final TCPConnectionManager senderManager;
    private final Socket socket;
    private ObjectOutputStream socketOutputStream;

    public TCPSenderThread(Socket socket, TCPConnectionManager senderManager) {
        this.socket = socket;
        this.senderManager = senderManager;
    }

    public void run() {
        try {
            socketOutputStream = new ObjectOutputStream(socket.getOutputStream());
        } catch (Exception e) {
//            senderManager.getSenderThreads().remove(this);
            e.printStackTrace();
        }
    }

    public ObjectOutputStream getOutputStream() {
        return this.socketOutputStream;
    }
}
