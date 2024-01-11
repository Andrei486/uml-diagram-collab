package carleton.sysc4907.communications;

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

        }
    }
}
