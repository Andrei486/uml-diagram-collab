package carleton.sysc4907.communications;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class provides some of base componets shared between the hostManager and the clientManger
 */
public abstract class Manager {

    protected LinkedBlockingQueue<TargetedMessage> sendingQueue;
    protected MessageInterpreter messageInterpreter;
    protected MessageConstructor messageConstructor;


    public void setMessageInterpreter(MessageInterpreter messageInterpreter) {
        this.messageInterpreter = messageInterpreter;
    }

    /**
     * adds message to the message queue to be sent out by the Mangers TCPSender
     * @param targetedMessage the message to be sent
     */
    public void send(TargetedMessage targetedMessage) {
        sendingQueue.add(targetedMessage);
    }

    /**
     * Used to close all the connection the manager manages
     */
    public abstract void close();
}
