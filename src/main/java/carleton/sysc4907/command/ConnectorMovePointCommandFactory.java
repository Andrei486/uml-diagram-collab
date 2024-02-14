package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ConnectorMovePointCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;

public class ConnectorMovePointCommandFactory extends TrackedCommandFactory<Command<ConnectorMovePointCommandArgs>, ConnectorMovePointCommandArgs> {

    private final ElementIdManager elementIdManager;

    public ConnectorMovePointCommandFactory(ElementIdManager elementIdManager,
                                            Manager manager, ExecutedCommandList executedCommandList) {
        super(manager, executedCommandList);
        this.elementIdManager = elementIdManager;
    }

    /**
     * Creates a command object
     *
     * @param args the arguments for the command
     * @return a command of the specified type
     */
    @Override
    public Command<ConnectorMovePointCommandArgs> create(ConnectorMovePointCommandArgs args) {
        return new ConnectorMovePointCommand(args, elementIdManager);
    }
}
