package carleton.sysc4907.command;

import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.EditTextCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;

import java.lang.reflect.Member;

public class EditTextCommandFactory extends TrackedCommandFactory<Command<EditTextCommandArgs>, EditTextCommandArgs> {

    private final ElementIdManager elementIdManager;

    public EditTextCommandFactory(ElementIdManager elementIdManager,
                                  Manager manager, ExecutedCommandList executedCommandList,
                                  MessageConstructor messageConstructor) {
        super(manager, executedCommandList, messageConstructor);
        this.elementIdManager = elementIdManager;
    }

    @Override
    public Command<EditTextCommandArgs> create(EditTextCommandArgs args) {
        return new EditTextCommand(args, elementIdManager);
    }
}
