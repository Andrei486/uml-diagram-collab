package carleton.sysc4907.communications;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;

public class ClientList {

    private HashMap<Long, ClientData> clients;
    private MessageInterpreter messageInterpreter;

    public ClientList(MessageInterpreter messageInterpreter) {
        clients = new HashMap<>();
        this.messageInterpreter =  messageInterpreter;
    }

    public HashMap<Long, ClientData> getClients() {
        return clients;
    }

    public void addClient(Socket socket) throws IOException {
        clients.put((long) socket.getPort(), new ClientData(socket.getPort(), socket, this.messageInterpreter));
    }

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
}
