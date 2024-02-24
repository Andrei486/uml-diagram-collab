package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;

public class ChangeTextStyleCommandFactory extends TrackedCommandFactory<Command<ChangeTextStyleCommandArgs>, ChangeTextStyleCommandArgs>{

    private final ElementIdManager elementIdManager;

    public ChangeTextStyleCommandFactory(ElementIdManager elementIdManager,
                                         Manager manager,
                                         ExecutedCommandList executedCommandList) {
        super(manager, executedCommandList);
        this.elementIdManager = elementIdManager;
    }

    @Override
    public Command<ChangeTextStyleCommandArgs> create(ChangeTextStyleCommandArgs args) {
        return new ChangeTextStyleCommand(args, elementIdManager);
    }
}
