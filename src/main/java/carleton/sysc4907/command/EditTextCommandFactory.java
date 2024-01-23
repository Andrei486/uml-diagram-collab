package carleton.sysc4907.command;

import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.EditTextCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.processing.ElementIdManager;

public class EditTextCommandFactory extends TrackedCommandFactory<Command<EditTextCommandArgs>, EditTextCommandArgs> {

    private final ElementIdManager elementIdManager;

    public EditTextCommandFactory(ElementIdManager elementIdManager, Manager manager) {
        super(manager);
        this.elementIdManager = elementIdManager;
    }

    @Override
    public Command<EditTextCommandArgs> create(EditTextCommandArgs args) {
        return new EditTextCommand(args, elementIdManager);
    }
}
