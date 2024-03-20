package carleton.sysc4907.communications;

/**
 * Constructs messages to be sent
 */
public class MessageConstructor {

    private Manager manager;
    private ClientList clients;

    /**
     * constructs a message sender
     */
    public MessageConstructor() {

    }

    /**
     * sets both the manager and client list
     * @param manager the connecting manager
     * @param clients the ClientList of that manager
     */
    public void setManager(Manager manager, ClientList clients) {
        this.manager = manager;
        this.clients = clients;
    }


    /**
     * Wrap a message in a TargetedMessage and place it into the managers sending queue,
     * that will send to all valid clients
     * @param message the message to be wrapped
     */
    public void send(Message message) {
        manager.send(new TargetedMessage(new long[0], false, false, message));
    }

    /**
     * Wrap a message in a TargetedMessage and place it into the managers sending queue,
     * that will send to all clients
     * @param message the message to be wrapped
     */
    public void sendInvalid(Message message) {manager.send(new TargetedMessage(new long[0], true, false, message));}

    /**
     * Wrap a message in a TargetedMessage and place it into the managers sending queue,
     * that will send to a valid client
     * @param message the message to be wrapped
     * @param ids the ids of the clients
     */
    public void sendTo(Message message, long[] ids) {
        manager.send(new TargetedMessage(ids, false, false, message));
    }

    /**
     * Wrap a message in a TargetedMessage and place it into the managers sending queue,
     * that will send to a valid client
     * @param message the message to be wrapped
     * @param id the id of the client
     */
    public void sendTo(Message message, long id) {
        sendTo(message, new long[]{id});
    }

    /**
     * Wrap a message in a TargetedMessage and place it into the managers sending queue,
     * that will send to a client
     * @param message the message to be wrapped
     * @param ids the ids of the clients
     */
    public void sendToInvalid(Message message, long[] ids) {
        manager.send(new TargetedMessage(ids, true, false, message));
    }

    /**
     * Wrap a message in a TargetedMessage and place it into the managers sending queue,
     * that will send to a client
     * @param message the message to be wrapped
     * @param id the id of the client
     */
    public void sendToInvalid(Message message, long id) {
        sendToInvalid(message, new long[]{id});
    }

    /**
     * Wrap a message in a TargetedMessage and place it into the managers sending queue,
     * that will send to a client
     * @param message the message to be wrapped
     * @param ids the ids of the clients
     */
    public void sendToInvalidAndClose(Message message, long[] ids) {
        manager.send(new TargetedMessage(ids, true, true, message));
    }

    /**
     * Wrap a message in a TargetedMessage and place it into the managers sending queue,
     * that will send to a client
     * @param message the message to be wrapped
     * @param id the id of the client
     */
    public void sendToInvalidAndClose(Message message, long id) {
        sendToInvalidAndClose(message, new long[]{id});
    }

    /**
     * Wrap a message in a TargetedMessage and place it into the managers sending queue,
     * that will send to all user but the user provided.
     * NOTE: Will not send if no users are left after removing the user
     * @param message the message to be wrapped
     * @param id the id of the user not to be sent to
     */
    public void sendAllBut(Message message, long id) {
        long[] ids = clients.getClientIds(new long[]{id});

        if (ids.length > 0) {
            manager.send(new TargetedMessage(ids, false, false, message));
        }
    }


}
