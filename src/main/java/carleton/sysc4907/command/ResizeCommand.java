package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ResizeCommandArgs;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.layout.Pane;

public class ResizeCommand implements Command<ResizeCommandArgs> {

    private final ResizeCommandArgs args;

    public ResizeCommand(ResizeCommandArgs args) {
        this.args = args;
    }

    @Override
    public void execute() {
        var element = args.element();
        var dragStartX = args.dragStartX();
        var dragStartY = args.dragStartY();
        var dragEndX = args.dragEndX();
        var dragEndY = args.dragEndY();
        var isTopAnchor = args.isTopAnchor();
        var isRightAnchor = args.isRightAnchor();

        double widthChange = dragEndX - dragStartX;
        widthChange = isRightAnchor ? widthChange : -widthChange;
        widthChange = Math.max(widthChange, -element.getMaxWidth() + 20);
        double heightChange = dragEndY - dragStartY;
        heightChange = isTopAnchor ? -heightChange : heightChange;
        heightChange = Math.max(heightChange, -element.getMaxHeight() + 20);
        element.setMaxWidth(element.getMaxWidth() + widthChange);
        element.setMaxHeight(element.getMaxHeight() + heightChange);
        if (isTopAnchor) {
            element.setLayoutY(element.getLayoutY() - heightChange);
        }
        if (!isRightAnchor) {
            element.setLayoutX(element.getLayoutX() - widthChange);
        }
    }
}