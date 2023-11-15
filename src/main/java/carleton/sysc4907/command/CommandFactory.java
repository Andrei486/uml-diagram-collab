package carleton.sysc4907.command;

public interface CommandFactory<T extends Command<TArgs>, TArgs> {

    Command create(TArgs args);
}