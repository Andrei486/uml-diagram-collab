package carleton.sysc4907.communications;

import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class manages to connect between the host and the clients
 */
public class HostManager extends Manager {
    private TCPSender sender;
    private HostConnectionManager hostConnectionManager;
    private Thread senderThread;

    /**
     * constructs a ClientManager, sets up all the support classes and
     * prepares accept connections from ClientManagers
     * @param port the port to be opened
     * @param messageInterpreter the MessageInterpreter of the system
     * @param messageConstructor the MessageConstructor of the system
     * @throws IOException the connections can not be set up
     */
    public HostManager(
            int port,
            MessageInterpreter messageInterpreter,
            MessageConstructor messageConstructor)
            throws IOException
    {
        this.isHost = true;
        messageInterpreter.setManager(this);
        this.clientList = new ClientList(messageInterpreter);
        this.hostConnectionManager = new HostConnectionManager(port, this.clientList, this);
        this.sendingQueue = new LinkedBlockingQueue<TargetedMessage>();
        this.sender = new TCPSender(this.sendingQueue, this.clientList, this);
        messageConstructor.setManager(this, clientList);

        new Thread(hostConnectionManager).start();
        this.senderThread = new Thread(sender);
        this.senderThread.start();
    }

    @Override
    public void close() {
        sendingQueue.clear();
        long[] ids = clientList.getClients().keySet().stream().mapToLong(x -> (long) x).toArray();
        send(new TargetedMessage(ids, true, true,
                new Message(MessageType.CLOSE, "I have been Closed")));
        try {
            hostConnectionManager.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        senderThread.interrupt();
    }
}
