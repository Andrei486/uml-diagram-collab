package carleton.sysc4907.communications;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class HostManager extends Manager {
    private ClientList clientList;
    private TCPSender sender;
    private HostConnectionManager hostConnectionManager;


    public HostManager(int port) throws IOException {
        this.clientList = new ClientList();
        this.hostConnectionManager = new HostConnectionManager(port, this);
        this.sender = new TCPSender(new LinkedBlockingQueue<TargetedMessage>(), this);
    }

    public void addClient(Socket socket) {
        try {
            clientList.addClient(socket);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
