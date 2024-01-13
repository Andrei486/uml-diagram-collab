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
        this.hostConnectionManager = new HostConnectionManager(port, this.clientList, this);
        this.sender = new TCPSender(new LinkedBlockingQueue<TargetedMessage>(), this.clientList, this);
    }




    @Override
    public void getClientOutputStreams() {
        clientList.getOutputStreams();
    }
}
