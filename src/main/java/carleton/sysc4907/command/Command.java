package carleton.sysc4907.command;

import carleton.sysc4907.command.args.CommandArgs;

/**
 * Interface to define the command class for the command pattern
 * @param <TArgs> The type of arguments required for the command
 */
public interface Command<TArgs extends CommandArgs> {

    /**
     * Executes the command
     */
    public void execute();
    public TArgs getArgs();
}