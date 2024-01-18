package carleton.sysc4907.command;

import carleton.sysc4907.command.args.EditTextCommandArgs;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.scene.control.Label;

/**
 * Command for editing text inside an EditableLabel.
 */
public class EditTextCommand implements Command<EditTextCommandArgs> {

    private final EditTextCommandArgs args;
    private final ElementIdManager elementIdManager;

    /**
     * Constructs a new EditTextCommand.
     * @param args the command arguments
     * @param elementIdManager the element ID manager, used to find elements by ID
     */
    public EditTextCommand(EditTextCommandArgs args, ElementIdManager elementIdManager) {
        this.args = args;
        this.elementIdManager = elementIdManager;
    }

    /**
     * Executes the command
     */
    @Override
    public void execute() {
        var element = elementIdManager.getElementById(args.elementId());
        if (element == null) return;
        Label label = (Label) element;
        label.setText(args.text());
    }

    @Override
    public EditTextCommandArgs getArgs() {
        return args;
    }
}
