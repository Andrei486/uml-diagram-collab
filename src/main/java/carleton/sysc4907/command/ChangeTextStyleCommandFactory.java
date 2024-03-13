package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.EditableLabelTracker;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;

/**
 * Factory to instantiate ChangeTextStyleCommands.
 */
public class ChangeTextStyleCommandFactory extends TrackedCommandFactory<Command<ChangeTextStyleCommandArgs>, ChangeTextStyleCommandArgs>{

    private final ElementIdManager elementIdManager;
    private final EditableLabelTracker editableLabelTracker;

    /**
     * Constructor for the ChangeTextStyleCommandFactory.
     * @param elementIdManager the element ID manager.
     * @param manager the TCP manager.
     * @param executedCommandList the list of executed commands.
     * @param messageConstructor the message constructor.
     */
    public ChangeTextStyleCommandFactory(ElementIdManager elementIdManager,
                                         Manager manager,
                                         ExecutedCommandList executedCommandList,
                                         MessageConstructor messageConstructor,
                                         EditableLabelTracker editableLabelTracker) {
        super(manager, executedCommandList, messageConstructor);
        this.elementIdManager = elementIdManager;
        this.editableLabelTracker = editableLabelTracker;
    }

    /**
     * Creates a ChangeTextStyleCommand with the provided args. It is not a tracked command.
     * @param args the arguments for the command.
     * @return a ChangeTextStyleCommand.
     */
    @Override
    public Command<ChangeTextStyleCommandArgs> create(ChangeTextStyleCommandArgs args) {
        return new ChangeTextStyleCommand(args, elementIdManager, editableLabelTracker);
    }
}
