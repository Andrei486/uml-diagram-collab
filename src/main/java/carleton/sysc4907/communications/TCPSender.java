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

    private void send(ObjectOutputStream outputStream, Message message, long id){
        try {
            outputStream.writeObject(message);
        } catch (IOException e) {
            clients.removeClient(id);
        }
    }

    private void broadcastSend(HashMap<Long, ObjectOutputStream> outputStreams, Message message, boolean closeUserOnSend) {
        if(closeUserOnSend) {
            for (long id : outputStreams.keySet()) {
                System.out.println(id);
                send(outputStreams.get(id), message, id);
                clients.removeClient(id);
            }
        } else {
            for (long id : outputStreams.keySet()) {
                System.out.println(id);
                send(outputStreams.get(id), message, id);
            }
        }
    }

    private void targetedSend(HashMap<Long, ObjectOutputStream> outputStreams, Message message, boolean closeUserOnSend, long[] targetIds) {
        if(closeUserOnSend){
            for (long id : outputStreams.keySet()) {
                if (Arrays.binarySearch(targetIds, id) >= 0) {
                    send(outputStreams.get(id), message, id);
                    clients.removeClient(id);
                }
            }
        } else {
            for (long id : outputStreams.keySet()) {
                if (Arrays.binarySearch(targetIds, id) >= 0) {
                    send(outputStreams.get(id), message, id);
                }
            }
        }
    }

    @Override
    public void run() {
        boolean flag = true;
        while (flag) {
            TargetedMessage targetedMessage;
            if(Thread.currentThread().isInterrupted()) {
                return;
            }

            try {
                System.out.println("Sending Queue: Looking For Message");
                targetedMessage = sendQueue.take();
                System.out.println("Sending Queue: Found Message");
            } catch (InterruptedException e) {
                return;
            }


            Message message = targetedMessage.message();

            HashMap<Long, ObjectOutputStream> outputStreams;
            if (targetedMessage.allowUnauthorizedUsers()) {
                outputStreams = clients.getOutputStreams();
            } else {
                outputStreams = clients.getValidOutputStreams();
            }

            boolean closeUserOnSend = targetedMessage.closeUserOnSend();

            long[] targetIds = targetedMessage.receiverIds();
            if (targetIds == null || targetIds.length == 0) { //broadcast
                broadcastSend(outputStreams, message, closeUserOnSend);
            } else {
                targetedSend(outputStreams, message, closeUserOnSend, targetIds);
            }
        }
    }
}
