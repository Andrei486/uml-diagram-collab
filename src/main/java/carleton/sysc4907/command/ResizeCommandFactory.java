package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ResizeCommandArgs;
import carleton.sysc4907.processing.ElementIdManager;

public class ResizeCommandFactory implements CommandFactory<Command<ResizeCommandArgs>, ResizeCommandArgs> {

    private final ElementIdManager elementIdManager;

    public ResizeCommandFactory(ElementIdManager elementIdManager) {
        this.elementIdManager = elementIdManager;
    }
    @Override
    public Command create(ResizeCommandArgs args) {
        return new ResizeCommand(args, elementIdManager);
    }
}