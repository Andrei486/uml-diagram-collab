package carleton.sysc4907.command;

import carleton.sysc4907.command.args.CommandArgs;

/**
 * Interface for a command factory in the command pattern
 * @param <T> The command type
 * @param <TArgs> The type of arguments required for the command
 */
public interface CommandFactory<T extends Command<TArgs>, TArgs extends CommandArgs> {

    /**
     * Creates a command object
     * @param args the arguments for the command
     * @return a command of the specified type
     */
    Command<TArgs> create(TArgs args);

    /**
     * Creates a tracked command object
     * @param args the arguments for the tracked command
     * @return a tracked command that wraps a command of the specified type
     */
    Command<TArgs> createTracked(TArgs args);

    /**
     * Creates a remote command object, for commands received via TCP.
     * @param args the arguments for the remote command
     * @return a remote command wrapping a command of the specified type
     */
    Command<TArgs> createRemote(TArgs args);
}