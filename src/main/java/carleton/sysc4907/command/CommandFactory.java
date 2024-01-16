package carleton.sysc4907.command;

/**
 * Interface for a command factory in the command pattern
 * @param <T> The command type
 * @param <TArgs> The type of arguments required for the command
 */
public interface CommandFactory<T extends Command<TArgs>, TArgs> {

    /**
     * Creates a command object
     * @param args the arguments for the command
     * @return a command of the specified type
     */
    Command<TArgs> create(TArgs args);

    Command<TArgs> createTracked(TArgs args);
}