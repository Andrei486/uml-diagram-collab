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
     * Wrap a message in a TargetedMessage and place it into the managers sending queue
     * @param message the message to be wrapped
     */
    public void send(Message message) {
        manager.send(new TargetedMessage(new long[0], true, false, message));
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
            manager.send(new TargetedMessage(ids, true, false, message));
        }
    }
}
