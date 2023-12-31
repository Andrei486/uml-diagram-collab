package carleton.sysc4907.command;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.model.DiagramModel;

public class AddCommandFactory implements CommandFactory<Command<AddCommandArgs>, AddCommandArgs> {
    private final DiagramModel diagramModel;
    private final DependencyInjector elementInjector;

    public AddCommandFactory(DiagramModel diagramModel, DependencyInjector elementInjector) {
        this.diagramModel = diagramModel;
        this.elementInjector = elementInjector;
    }
    @Override
    public Command create(AddCommandArgs args) {
        return new AddCommand(args, diagramModel, elementInjector);
    }
}
