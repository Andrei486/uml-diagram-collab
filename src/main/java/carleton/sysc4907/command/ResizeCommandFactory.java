package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ResizeCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.processing.ElementIdManager;

public class ResizeCommandFactory extends TrackedCommandFactory<Command<ResizeCommandArgs>, ResizeCommandArgs> {

    private final ElementIdManager elementIdManager;

    public ResizeCommandFactory(ElementIdManager elementIdManager, Manager manager) {
        super(manager);
        this.elementIdManager = elementIdManager;
    }
    @Override
    public Command<ResizeCommandArgs> create(ResizeCommandArgs args) {
        return new ResizeCommand(args, elementIdManager);
    }
}