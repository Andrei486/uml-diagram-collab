package carleton.sysc4907.communications;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
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

    public void addClient(Socket socket) throws IOException {
        clients.put(socket.getPort(), new ClientData(socket.getPort(), socket));
    }

    public HashMap<Integer, ObjectOutputStream> getOutputStreams() {
        HashMap<Integer, ObjectOutputStream> outputs = new HashMap<>();

        for (ClientData client: clients.values()) {
            outputs.put(client.getId(), client.getOutputStream());
        }

        return outputs;
    }
}
