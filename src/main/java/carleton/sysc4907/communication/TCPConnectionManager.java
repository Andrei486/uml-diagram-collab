package carleton.sysc4907.communication;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

/**
 * A class to manage all the various TCP senders. Handles logic for sending messages to some or all of the other users.
 */
public class TCPConnectionManager implements Runnable {

    private final ServerSocket serverSocket;

    private final Map<Long, ObjectOutputStream> outputStreams = new HashMap<>();

    private final Set<Long> authorizedIds = new HashSet<>(); // add here on accept join, remove on leave

    public TCPConnectionManager(int portNumber) throws IOException {
        serverSocket = new ServerSocket(portNumber);
    }

    public int getPort() {
        return serverSocket.getLocalPort();
    }

    public void run() {
        try {
            while (true) {
                Socket socket = serverSocket.accept();
                outputStreams.put((long) socket.getPort(), new ObjectOutputStream(socket.getOutputStream()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Collection<ObjectOutputStream> getConnections() {
        return outputStreams.values();
    }

    public Set<Long> getIds() {
        return outputStreams.keySet();
    }

    public ObjectOutputStream getConnectionById(long id) {
        return outputStreams.get(id);
    }

    /**
     * Checks whether there is an authorized connection with the given ID, i.e. a connection where the user's join
     * has been accepted. A connection to the host is always authorized.
     * @param id the ID of the connection to check
     * @return true if there is such an authorized connection, false otherwise
     */
    public boolean isAuthorized(long id) {
        return authorizedIds.contains(id) && outputStreams.containsKey(id);
    }
}
