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

public abstract class Manager {

    protected LinkedBlockingQueue<TargetedMessage> sendingQueue;
    protected MessageInterpreter messageInterpreter;

    public void setMessageInterpreter(MessageInterpreter messageInterpreter) {
        this.messageInterpreter = messageInterpreter;
    }

    public void send(TargetedMessage targetedMessage) {
        sendingQueue.add(targetedMessage);
    }
}
