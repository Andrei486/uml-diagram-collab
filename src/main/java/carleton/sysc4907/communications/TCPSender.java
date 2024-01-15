package carleton.sysc4907.communications;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

public class TCPSender implements Runnable{
    private final BlockingQueue<TargetedMessage> sendQueue;
    private final Manager manager;
    private ClientList clients;

    public TCPSender(BlockingQueue<TargetedMessage> sendQueue, ClientList clients, Manager manager) {
        this.sendQueue = sendQueue;
        this.clients = clients;
        this.manager = manager;
    }

    private void send(ObjectOutputStream outputStream, Message message) throws IOException {
        outputStream.writeObject(message);
    }


    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            TargetedMessage targetedMessage;
            try {
                System.out.println("Sending Queue: Looking For Message");
                targetedMessage = sendQueue.take();
                System.out.println("Sending Queue: Found Message");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            Message message = targetedMessage.message();

            HashMap<Long, ObjectOutputStream> outputStreams;
            if(targetedMessage.allowUnauthorizedUsers()) {
                outputStreams = clients.getOutputStreams();
            } else {
                outputStreams = clients.getValidOutputStreams();
            }

            long[] targetIds = targetedMessage.receiverIds();

            if (targetIds == null || targetIds.length == 0) { //broadcast
                for(long id: outputStreams.keySet()) {
                    try {
                        System.out.println(id);
                        send(outputStreams.get(id), message);
                    } catch (IOException e) {
                        clients.removeClient(id);
                    }
                }
            } else {
                for(long id: outputStreams.keySet()) {
                    if (Arrays.binarySearch(targetIds, id) >= 0) {
                        try {
                            send(outputStreams.get(id), message);
                        } catch (IOException e) {
                            clients.removeClient(id);
                        }
                    }
                }
            }
        }
    }
}
