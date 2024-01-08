package carleton.sysc4907.command;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;

public class AddCommandFactory implements CommandFactory<Command<AddCommandArgs>, AddCommandArgs> {
    private final DiagramModel diagramModel;
    private final ElementCreator elementCreator;

    public AddCommandFactory(DiagramModel diagramModel, ElementCreator elementCreator) {
        this.diagramModel = diagramModel;
        this.elementCreator = elementCreator;
    }
    @Override
    public Command create(AddCommandArgs args) {
        return new AddCommand(args, diagramModel, elementCreator);
    }
}
