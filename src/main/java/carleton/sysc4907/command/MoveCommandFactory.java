package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementIdManager;

public class MoveCommandFactory implements CommandFactory<Command<MoveCommandArgs>, MoveCommandArgs> {

    private final ElementIdManager elementIdManager;

    public MoveCommandFactory(ElementIdManager elementIdManager) {
        this.elementIdManager = elementIdManager;
    }

    @Override
    public Command create(MoveCommandArgs args) {
        return new MoveCommand(args, elementIdManager);
    }
}