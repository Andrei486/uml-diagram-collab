package carleton.sysc4907.communications;

import carleton.sysc4907.model.SessionModel;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class provides some of base componets shared between the hostManager and the clientManger
 */
public abstract class Manager {

    protected LinkedBlockingQueue<TargetedMessage> sendingQueue;
    protected MessageInterpreter messageInterpreter;
    protected MessageConstructor messageConstructor;
    protected ClientList clientList;
    protected boolean isHost;
    protected int port;


    public void setMessageInterpreter(MessageInterpreter messageInterpreter) {
        this.messageInterpreter = messageInterpreter;
    }

    /**
     * adds message to the message queue to be sent out by the Mangers TCPSender
     * @param targetedMessage the message to be sent
     */
    public void send(TargetedMessage targetedMessage) {
        if (clientList.getClients().size() > 0) {
            sendingQueue.add(targetedMessage);
        } else {
            System.out.println("No Clients to Send too");
        }
    }

    /**
     * Validate a user
     * @param id the id of the user being validated
     */
    public void validateClient(long id) {
        clientList.getClient(id).setValid(true);
    }

    public ClientList getClientList() {
        return clientList;
    }

    public boolean isHost() {
        return isHost;
    }

    public int getPort() {
        return port;
    }

    /**
     * Used to close all the connection the manager manages
     */
    public abstract void close();
}
