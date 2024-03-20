package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ChangeConnectorStyleCommandArgs;
import carleton.sysc4907.command.args.ConnectorMovePointCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;

public class ChangeConnectorStyleCommandFactory extends TrackedCommandFactory<Command<ChangeConnectorStyleCommandArgs>, ChangeConnectorStyleCommandArgs> {

    private final ElementIdManager elementIdManager;

    public ChangeConnectorStyleCommandFactory(ElementIdManager elementIdManager,
                                              Manager manager, ExecutedCommandList executedCommandList,
                                              MessageConstructor messageConstructor) {
        super(manager, executedCommandList, messageConstructor);
        this.elementIdManager = elementIdManager;
    }

    /**
     * Creates a command object
     *
     * @param args the arguments for the command
     * @return a command of the specified type
     */
    @Override
    public Command<ChangeConnectorStyleCommandArgs> create(ChangeConnectorStyleCommandArgs args) {
        return new ChangeConnectorStyleCommand(args, elementIdManager);
    }
}
