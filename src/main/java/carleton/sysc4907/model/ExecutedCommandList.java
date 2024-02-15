package carleton.sysc4907.model;

import carleton.sysc4907.command.Command;

import java.util.LinkedList;
import java.util.List;

public class ExecutedCommandList {

    private final List<Command<?>> commands;

    public ExecutedCommandList(List<Command<?>> commands) {
        this.commands = commands;
    }

    public ExecutedCommandList() {
        this(new LinkedList<>());
    }

    public List<Command<?>> getCommandList() {
        return commands;
    }
}
