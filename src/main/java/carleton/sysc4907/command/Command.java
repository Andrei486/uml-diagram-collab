package carleton.sysc4907.command;

/**
 * Interface to define the command class for the command pattern
 * @param <TArgs> The type of arguments required for the command
 */
public interface Command<TArgs> {

    /**
     * Executes the command
     */
    public void execute();
}