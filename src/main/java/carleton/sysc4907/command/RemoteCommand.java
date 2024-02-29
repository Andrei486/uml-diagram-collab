package carleton.sysc4907.command;

import carleton.sysc4907.command.args.CommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.Message;
import carleton.sysc4907.communications.MessageType;
import carleton.sysc4907.communications.TargetedMessage;
import carleton.sysc4907.model.ExecutedCommandList;
import javafx.application.Platform;

import java.time.LocalTime;

public class RemoteCommand<TArgs extends CommandArgs> implements Command<TArgs> {

    private final Command<TArgs> command;
    private final ExecutedCommandList executedCommandList;

    /**
     * Creates a new tracked command to wrap another command.
     * @param command the command to wrap.
     */
    public RemoteCommand(Command<TArgs> command, ExecutedCommandList executedCommandList) {
        this.command = command;
        this.executedCommandList = executedCommandList;
    }

    public TArgs getArgs() {
        return command.getArgs();
    }

    @Override
    public void execute() {
        // Add the command to the running queue
        Platform.runLater(command::execute);
        executedCommandList.getCommandList().add(command); // When a command is received (including back to client) add it
        System.out.println("Remote command finished executing (on platform) at time " + LocalTime.now());
    }
}
