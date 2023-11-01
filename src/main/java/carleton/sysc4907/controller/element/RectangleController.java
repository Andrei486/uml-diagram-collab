package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.Command;
import carleton.sysc4907.command.MoveCommand;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.DiagramElement;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class RectangleController {

    @FXML
    private DiagramElement element;

    @FXML
    private Rectangle rect;

    private double dragStartX = 0;
    private double dragStartY = 0;

    private boolean dragging = false;

    public RectangleController() {

    }

    @FXML
    public void initialize() {
        element.addEventHandler(MouseEvent.ANY, (event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                dragging = false;
                dragStartX = event.getSceneX() - element.getLayoutX();
                dragStartY = event.getSceneY() - element.getLayoutY();
            }
            else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
                dragging = true;
            }
            else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                double dragEndX = event.getSceneX();
                double dragEndY = event.getSceneY();
                // these two lines need to be put into a factory ASAP
                MoveCommandArgs args = new MoveCommandArgs(
                        dragStartX, dragStartY,
                        dragEndX, dragEndY,
                        element);
                MoveCommand command = new MoveCommand(args);
                command.execute();
            }
        }));
    }
}
