package carleton.sysc4907.command;

import carleton.sysc4907.communications.Manager;

/**
 * A factory to instantiate tracked commands.
 * @param <T> The type of commands created by the factory.
 * @param <TArgs> The arg type the command takes.
 */
public abstract class TrackedCommandFactory<T extends Command<TArgs>, TArgs> implements CommandFactory<T, TArgs>{

    private final Manager manager;
    public TrackedCommandFactory(Manager manager) {
        this.manager = manager;
    }
    @Override
    public Command<TArgs> createTracked(TArgs args) {
        return new TrackedCommand<>(create(args), manager);
    }
}
