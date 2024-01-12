package carleton.sysc4907.communications;

import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class TCPSender implements Runnable{
    private final BlockingQueue<TargetedMessage> sendQueue;
    private final Manager manager;

    public TCPSender(BlockingQueue<TargetedMessage> sendQueue, Manager manager) {
        this.sendQueue = sendQueue;
        this.manager = manager;
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
                        // IO exception will occur if the connection is closed, so remove it
                        connectionManager.removeConnectionById(id);
                    }
                }
            }

        }
    }
}
