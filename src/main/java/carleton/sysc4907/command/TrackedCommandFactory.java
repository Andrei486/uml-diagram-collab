package carleton.sysc4907.command;

import carleton.sysc4907.communications.Manager;

public abstract class TrackedCommandFactory<T extends Command<TArgs>, TArgs> implements CommandFactory<T, TArgs>{

    private Manager manager;
    public TrackedCommandFactory(Manager manager) {
        this.manager = manager;
    }
    @Override
    public Command<TArgs> createTracked(TArgs args) {
        return new TrackedCommand<>(create(args), manager);
    }
}
