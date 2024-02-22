package carleton.sysc4907.command;

import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.model.ExecutedCommandList;

/**
 * A factory to instantiate tracked commands.
 * @param <T> The type of commands created by the factory.
 * @param <TArgs> The arg type the command takes.
 */
public abstract class TrackedCommandFactory<T extends Command<TArgs>, TArgs> implements CommandFactory<T, TArgs>{

    private final Manager manager;
    private final ExecutedCommandList executedCommandList;
    public TrackedCommandFactory(Manager manager, ExecutedCommandList executedCommandList) {
        this.manager = manager;
        this.executedCommandList = executedCommandList;
    }
    @Override
    public Command<TArgs> createTracked(TArgs args) {
        return new TrackedCommand<>(create(args), manager, executedCommandList);
    }

    @Override
    public Command<TArgs> createRemote(TArgs args) {
        return new RemoteCommand<>(create(args), executedCommandList);
    }
}
