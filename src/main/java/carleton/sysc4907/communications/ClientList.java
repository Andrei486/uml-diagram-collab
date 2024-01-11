package carleton.sysc4907.communications;

import java.util.ArrayList;
import java.util.HashMap;

public class ClientList {

    private HashMap<Integer, ClientData> clients;

    public ClientList() {
        clients = new HashMap<>();
    }

    public HashMap<Integer, ClientData> getClients() {
        return clients;
    }
}
