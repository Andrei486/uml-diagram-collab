package carleton.sysc4907.command;

import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.Message;
import carleton.sysc4907.communications.MessageType;
import carleton.sysc4907.communications.TargetedMessage;
import javafx.application.Platform;

public class TrackedCommand<TArgs> implements Command<TArgs>{

    private final Command<TArgs> command;
    private final Manager manager;

    public TrackedCommand(Command<TArgs> command, Manager manager) {
        this.command = command;
        this.manager = manager;
    }

    public TArgs getArgs() {
        return command.getArgs();
    }

    @Override
    public void execute() {
        //Add the command to the running queue
        Platform.runLater(command::execute);

        //Create a message to send over TCP
        TargetedMessage message = new TargetedMessage(
                new long[0],
                true,
                new Message(MessageType.UPDATE, command.getArgs())
        );
        manager.send(message);
    }
}
