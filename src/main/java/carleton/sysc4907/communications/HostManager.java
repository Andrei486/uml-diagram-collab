package carleton.sysc4907.communications;

import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

public class HostManager extends Manager {
    private ClientList clientList;
    private TCPSender sender;
    private HostConnectionManager hostConnectionManager;



    public HostManager(
            int port,
            MessageInterpreter messageInterpreter)
            throws IOException
    {
        this.clientList = new ClientList(messageInterpreter);
        this.hostConnectionManager = new HostConnectionManager(port, this.clientList, this);
        this.sendingQueue = new LinkedBlockingQueue<TargetedMessage>();
        this.sender = new TCPSender(this.sendingQueue, this.clientList, this);

        new Thread(hostConnectionManager).start();
        new Thread(sender).start();
    }
}
