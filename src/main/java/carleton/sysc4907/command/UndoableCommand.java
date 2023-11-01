package carleton.sysc4907.command;

public interface UndoableCommand<TArgs> extends Command<TArgs> {
    public void undo();
}
