package carleton.sysc4907.communications;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * The list of clients and their data managed by the manager
 */
public class ClientList {

    private HashMap<Long, ClientData> clients;
    private MessageInterpreter messageInterpreter;

    /**
     * Creates a list of client data
     * @param messageInterpreter the hostManagers messageInterpreter
     */
    public ClientList(MessageInterpreter messageInterpreter) {
        clients = new HashMap<>();
        this.messageInterpreter =  messageInterpreter;
    }

    public HashMap<Long, ClientData> getClients() {
        return clients;
    }

    /**
     * Creates of a clientData for a client and adds it to the map,
     * the socket port number is used as the id
     * @param socket the socket of the client
     * @throws IOException the connection failed
     */
    public void addClient(Socket socket) throws IOException {
        clients.put((long) socket.getPort(), new ClientData(socket.getPort(), socket, this.messageInterpreter));
    }

    /**
     * removes a client from the map
     * @param id the id of the client to be removed
     */
    public void removeClient(long id) {
        System.out.println("Closing Client: " + id);
        ClientData client = clients.get(id);
        if (client == null) {
            return;
        }
        clients.remove(id);
        try {
            client.close();
        } catch (IOException e) {
            // client already closed
        }
    }

    /**
     * Gets the output streams for all the clients
     * @return a map of all ids and output streams for the clients in a map
     */
    public HashMap<Long, ObjectOutputStream> getOutputStreams() {
        HashMap<Long, ObjectOutputStream> outputs = new HashMap<>();

        for (ClientData client: clients.values()) {
            outputs.put(client.getId(), client.getOutputStream());
        }

        return outputs;
    }

    public HashMap<Long, ObjectOutputStream> getValidOutputStreams() {
        HashMap<Long, ObjectOutputStream> outputs = new HashMap<>();

        for (ClientData client: clients.values()) {
            if(client.getValid()) {
                outputs.put((long) client.getId(), client.getOutputStream());
            }
        }

        return outputs;
    }

    /**
     * get the ids of the clients in the client list
     * @param idsToBeRemove the ids to be removed from the list
     * @return a long array of ids with the ids to be removed, removed
     */
    public long[] getClientIds(long[] idsToBeRemove) {
        ArrayList<Long> ids = new ArrayList<>();

        for (long id : clients.keySet()) {
            if (!(Arrays.binarySearch(idsToBeRemove, id) >= 0)) {
                ids.add(id);
            }
        }

        return Arrays.stream(ids.toArray()).mapToLong(x -> (long) x).toArray();
    }
}
