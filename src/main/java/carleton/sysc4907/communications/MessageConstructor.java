package carleton.sysc4907.communications;

public class MessageConstructor {

    private Manager manager;
    private ClientList clients;

    public MessageConstructor() {

    }

    public void setManager(Manager manager, ClientList clients) {
        this.manager = manager;
        this.clients = clients;
    }


    public void send(Message message) {
        manager.send(new TargetedMessage(new long[0], true, false, message));
    }

    public void sendAllBut(Message message, long id) {
        long[] ids = clients.getClientIds(new long[]{id});

        if (ids.length > 0) {
            manager.send(new TargetedMessage(ids, true, false, message));
        }
    }
}
