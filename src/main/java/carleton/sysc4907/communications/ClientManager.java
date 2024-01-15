package carleton.sysc4907.communications;

import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class ClientManager extends Manager{

    private ClientList clientList;
    private TCPSender sender;
    private ClientConnectionManager clientConnectionManger;

    public ClientManager(
            int port,
            String ip,
            DiagramModel diagramModel,
            ElementCreator elementCreator,
            ElementIdManager elementIdManager)
            throws IOException {
        this.clientList = new ClientList(makeMessageInterpreter(diagramModel, elementCreator, elementIdManager));
        this.clientConnectionManger = new ClientConnectionManager(ip, port, this.clientList);
        this.sendingQueue = new LinkedBlockingQueue<TargetedMessage>();
        this.sender = new TCPSender(this.sendingQueue, this.clientList, this);

        new Thread(sender).start();
    }
}
