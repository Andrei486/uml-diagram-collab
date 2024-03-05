package carleton.sysc4907.command;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementCreator;

public class AddCommandFactory extends TrackedCommandFactory<Command<AddCommandArgs>, AddCommandArgs> {
    private final DiagramModel diagramModel;
    private final ElementCreator elementCreator;

    public AddCommandFactory(DiagramModel diagramModel, ElementCreator elementCreator,
                             Manager manager, ExecutedCommandList executedCommandList,
                             MessageConstructor messageConstructor) {
        super(manager, executedCommandList, messageConstructor);
        this.diagramModel = diagramModel;
        this.elementCreator = elementCreator;
    }
    @Override
    public Command<AddCommandArgs> create(AddCommandArgs args) {
        return new AddCommand(args, diagramModel, elementCreator);
    }
}
