package carleton.sysc4907.command;

import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementIdManager;

public class RemoveCommandFactory implements CommandFactory<Command<RemoveCommandArgs>, RemoveCommandArgs> {
    private final DiagramModel diagramModel;
    private final ElementIdManager elementIdManager;

    public RemoveCommandFactory(DiagramModel diagramModel, ElementIdManager elementIdManager) {
        this.diagramModel = diagramModel;
        this.elementIdManager = elementIdManager;
    }

    @Override
    public Command create(RemoveCommandArgs args) {
        return new RemoveCommand(args, diagramModel, elementIdManager);
    }
}
