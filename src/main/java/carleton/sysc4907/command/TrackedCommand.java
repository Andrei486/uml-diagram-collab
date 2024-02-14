package carleton.sysc4907.command;

import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.Message;
import carleton.sysc4907.communications.MessageType;
import carleton.sysc4907.communications.TargetedMessage;
import carleton.sysc4907.model.ExecutedCommandList;
import javafx.application.Platform;

import java.time.LocalTime;

/**
 * A wrapper to make any type of command into a track command.
 * Tracked commands are put on the command stack and sent over TCP.
 * @param <TArgs> The type of args the command takes
 */
public class TrackedCommand<TArgs> implements Command<TArgs>{

    private final Command<TArgs> command;
    private final Manager manager;
    private final ExecutedCommandList executedCommandList;

    /**
     * Creates a new tracked command to wrap another command.
     * @param command the command to wrap.
     * @param manager the manager instance.
     */
    public TrackedCommand(Command<TArgs> command, Manager manager, ExecutedCommandList executedCommandList) {
        this.command = command;
        this.manager = manager;
        this.executedCommandList = executedCommandList;
    }

    public TArgs getArgs() {
        return command.getArgs();
    }

    @Override
    public void execute() {
        // Add the command to the running queue
        // Tracked commands must be sent to the host first, they will be run untracked by the client
        if (manager.isHost()) {
            Platform.runLater(command::execute);
            executedCommandList.getCommandList().add(command); // The host does not receive its command back so add it here
            System.out.println("Tracked command finished executing (on platform) at time " + LocalTime.now());
        }

        //Create a message to send over TCP
        TargetedMessage message = new TargetedMessage(
                new long[0],
                true,
                false,
                new Message(MessageType.UPDATE, command.getArgs())
        );
        manager.send(message);
    }
}
