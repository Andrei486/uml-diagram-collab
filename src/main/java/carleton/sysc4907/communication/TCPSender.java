package carleton.sysc4907.communication;


import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

/**
 * A class that takes a queue of messages to send, and sends them to the various connected hosts as they are put in the queue.
 */
public class TCPSender implements Runnable {

    private final BlockingQueue<TargetedMessage> sendQueue;
    private final TCPConnectionManager connectionManager;

    public TCPSender(BlockingQueue<TargetedMessage> sendQueue, TCPConnectionManager connectionManager) {
        this.sendQueue = sendQueue;
        this.connectionManager = connectionManager;
    }

    public void enqueue(TargetedMessage message) {
        sendQueue.add(message);
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            TargetedMessage targetedMessage;
            try {
                 targetedMessage = sendQueue.take();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            long[] targetIds = targetedMessage.receiverIds();
            if (targetIds == null || targetIds.length == 0) {
                targetIds = Arrays.stream(connectionManager.getIds().toArray()).mapToLong(x -> (Long) x).toArray();
            }
            Message message = targetedMessage.message();
            for (long id : targetIds) {
                if (targetedMessage.allowUnauthorizedUsers() || connectionManager.isAuthorized(id)) {
                    try {
                        connectionManager.getConnectionById(id).writeObject(message);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
