package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;

/**
 * Command for the move element operation
 */
public class MoveCommand implements Command<MoveCommandArgs> {

    private final MoveCommandArgs args;

    public MoveCommand(MoveCommandArgs args) {
        this.args = args;
    }

    @Override
    public void execute() {
        var element = args.element();
        element.setLayoutX(args.endX() - args.startX());
        element.setLayoutY(args.endY() - args.startY());
    }
}