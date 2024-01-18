package carleton.sysc4907.command;

import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementIdManager;

/**
 * Command for the move element operation
 */
public class MoveCommand implements Command<MoveCommandArgs> {

    private final MoveCommandArgs args;
    private final ElementIdManager elementIdManager;

    public MoveCommand(MoveCommandArgs args, ElementIdManager elementIdManager) {
        this.args = args;
        this.elementIdManager = elementIdManager;
    }

    @Override
    public void execute() {
        var element = elementIdManager.getElementById(args.elementId());
        if (element == null) return;
        element.setLayoutX(args.endX() - args.startX());
        element.setLayoutY(args.endY() - args.startY());
    }

    @Override
    public MoveCommandArgs getArgs() {
        return args;
    }
}