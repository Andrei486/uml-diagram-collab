package carleton.sysc4907.communications;

import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class manages to connect between the client and the host
 */
public class ClientManager extends Manager{

    private ClientList clientList;
    private TCPSender sender;
    private ClientConnectionManager clientConnectionManger;
    private Thread senderThread;

    /**
     * constructs a ClientManager, sets up all the support classes and
     * the connection to the HostManager
     * @param port the port of the HostManager
     * @param ip the ip of the HostManager
     * @param messageInterpreter the MessageInterpreter of the system
     * @param messageConstructor the MessageConstructor of the system
     * @throws IOException the connection can not be set up
     */
    public ClientManager(
            int port,
            String ip,
            MessageInterpreter messageInterpreter,
            MessageConstructor messageConstructor)
            throws IOException {

        messageInterpreter.setManager(this, false);
        this.clientList = new ClientList(messageInterpreter);
        this.clientConnectionManger = new ClientConnectionManager(ip, port, this.clientList);
        this.sendingQueue = new LinkedBlockingQueue<TargetedMessage>();
        this.sender = new TCPSender(this.sendingQueue, this.clientList, this);
        messageConstructor.setManager(this, clientList);

        this.senderThread = new Thread(sender);
        this.senderThread.start();
    }

    @Override
    public void close() {
        sendingQueue.clear();
        long[] ids = clientList.getClients().keySet().stream().mapToLong(x -> (long) x).toArray();
        send(new TargetedMessage(ids, true, true,
                new Message(MessageType.CLOSE, "I have been Closed")));

        senderThread.interrupt();
    }
}
