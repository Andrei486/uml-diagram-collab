package carleton.sysc4907.command;

public interface Command<TArgs> {

    public void execute();
}