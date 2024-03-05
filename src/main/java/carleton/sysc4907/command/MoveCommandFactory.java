package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;

public class MoveCommandFactory extends TrackedCommandFactory<Command<MoveCommandArgs>, MoveCommandArgs> {

    private final ElementIdManager elementIdManager;

    public MoveCommandFactory(ElementIdManager elementIdManager,
                              Manager manager, ExecutedCommandList executedCommandList,
                              MessageConstructor messageConstructor) {
        super(manager, executedCommandList, messageConstructor);
        this.elementIdManager = elementIdManager;
    }

    @Override
    public Command<MoveCommandArgs> create(MoveCommandArgs args) {
        return new MoveCommand(args, elementIdManager);
    }
}