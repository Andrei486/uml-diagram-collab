package carleton.sysc4907.communications;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.BlockingQueue;

/**
 * Sends messages to from the sendQueue to users
 */
public class TCPSender implements Runnable{
    private final BlockingQueue<TargetedMessage> sendQueue;
    private final Manager manager;
    private ClientList clients;

    /**
     * Constructs a TCPSender
     * @param sendQueue the manager's sendQueue
     * @param clients the manager's ClientList
     * @param manager the manager of the system
     */
    public TCPSender(BlockingQueue<TargetedMessage> sendQueue, ClientList clients, Manager manager) {
        this.sendQueue = sendQueue;
        this.clients = clients;
        this.manager = manager;
    }

    /**
     * Sends a message to another user
     * @param outputStream the output stream of the user
     * @param message the message being sent
     * @param id the id of the user
     */
    private void send(ObjectOutputStream outputStream, Message message, long id){
        try {
            outputStream.writeObject(message);
            System.out.println("Message sent at time " + LocalTime.now());
        } catch (IOException e) {
            System.out.println("Message Failed to Send to: " + id);
            clients.removeClient(id);
        }
    }

    /**
     * Used to send to every client connected to the manager
     * @param outputStreams the output streams of the clients
     * @param message the message to be sent
     * @param closeUserOnSend if the connection to the clients should be closed
     */
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

    /**
     * used to send to every specified client connected to the manager
     * @param outputStreams the output streams of the clients
     * @param message the message to be sent
     * @param closeUserOnSend if the connection to the clients should be closed
     * @param targetIds the ids of the clients to be sent to
     */
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
