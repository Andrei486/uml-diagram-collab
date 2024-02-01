package carleton.sysc4907.communications;

import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientManager extends Manager{

    private ClientList clientList;
    private TCPSender sender;
    private ClientConnectionManager clientConnectionManger;
    private Thread senderThread;

    public ClientManager(
            int port,
            String ip,
            MessageInterpreter messageInterpreter,
            MessageConstructor messageConstructor)
            throws IOException {
        this.isHost = false;
        messageInterpreter.setManager(this);
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
