package carleton.sysc4907.command;

import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;

public class RemoveCommandFactory extends TrackedCommandFactory<Command<RemoveCommandArgs>, RemoveCommandArgs> {
    private final DiagramModel diagramModel;
    private final ElementIdManager elementIdManager;

    public RemoveCommandFactory(DiagramModel diagramModel, ElementIdManager elementIdManager,
                                Manager manager, ExecutedCommandList executedCommandList,
                                MessageConstructor messageConstructor) {
        super(manager, executedCommandList, messageConstructor);
        this.diagramModel = diagramModel;
        this.elementIdManager = elementIdManager;
    }

    @Override
    public Command<RemoveCommandArgs> create(RemoveCommandArgs args) {
        return new RemoveCommand(args, diagramModel, elementIdManager);
    }
}
