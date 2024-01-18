package carleton.sysc4907.communications;
import carleton.sysc4907.command.*;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.command.args.ResizeCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;

import java.util.concurrent.LinkedBlockingQueue;

abstract class Manager {

    protected LinkedBlockingQueue<TargetedMessage> sendingQueue;
    public void send(TargetedMessage targetedMessage) {
        sendingQueue.add(targetedMessage);
    }

    abstract public void close();

    public MessageInterpreter makeMessageInterpreter
            (DiagramModel diagramModel,
             ElementCreator elementCreator,
             ElementIdManager elementIdManager)
    {
        return new MessageInterpreter(
                new AddCommandFactory(diagramModel, elementCreator),
                new RemoveCommandFactory(diagramModel, elementIdManager),
                new MoveCommandFactory(elementIdManager),
                new ResizeCommandFactory(elementIdManager));
    }



}
