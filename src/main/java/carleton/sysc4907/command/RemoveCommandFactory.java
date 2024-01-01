package carleton.sysc4907.command;

import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;

public class RemoveCommandFactory implements CommandFactory<Command<RemoveCommandArgs>, RemoveCommandArgs> {
    private final DiagramModel diagramModel;

    public RemoveCommandFactory(DiagramModel diagramModel) {
        this.diagramModel = diagramModel;
    }

    @Override
    public Command create(RemoveCommandArgs args) {
        return new RemoveCommand(args, diagramModel);
    }
}
