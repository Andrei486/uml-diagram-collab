package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.DiagramElement;

public class MoveCommand implements UndoableCommand<MoveCommandArgs> {

    private final MoveCommandArgs args;

    public MoveCommand(MoveCommandArgs args) {
        this.args = args;
    }

    @Override
    public void execute() {
        DiagramElement element = args.element();
        element.setLayoutX(args.endX() - args.startX());
        element.setLayoutY(args.endY() - args.startY());
    }

    @Override
    public void undo() {
        DiagramElement element = args.element();
        element.setLayoutX(args.startX() - args.endX());
        element.setLayoutY(args.startY() - args.endY());
    }
}
