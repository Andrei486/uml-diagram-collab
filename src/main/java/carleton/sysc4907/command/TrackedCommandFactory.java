package carleton.sysc4907.command;

import carleton.sysc4907.command.args.CommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.ExecutedCommandList;

/**
 * A factory to instantiate tracked commands.
 * @param <T> The type of commands created by the factory.
 * @param <TArgs> The arg type the command takes.
 */
public abstract class TrackedCommandFactory<T extends Command<TArgs>, TArgs extends CommandArgs> implements CommandFactory<T, TArgs>{

    private final Manager manager;
    private final ExecutedCommandList executedCommandList;
    private final MessageConstructor messageConstructor;
    public TrackedCommandFactory(Manager manager, ExecutedCommandList executedCommandList, MessageConstructor messageConstructor) {
        this.manager = manager;
        this.executedCommandList = executedCommandList;
        this.messageConstructor = messageConstructor;
    }
    @Override
    public Command<TArgs> createTracked(TArgs args) {
        return new TrackedCommand<>(create(args), manager, executedCommandList, messageConstructor);
    }

    @Override
    public Command<TArgs> createRemote(TArgs args) {
        return new RemoteCommand<>(create(args), executedCommandList);
    }
}
