package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommand;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.DiagramElement;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class RectangleController {

    @FXML
    private DiagramElement element;

    @FXML
    private Rectangle rect;

    private double dragStartX = 0;
    private double dragStartY = 0;

    private boolean dragging = false;

    private ImageView preview;

    public RectangleController() {

    }

    @FXML
    public void initialize() {
        element.addEventHandler(MouseEvent.ANY, (event -> {
            if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
                dragging = false;
            }
            else if (event.getEventType() == MouseEvent.DRAG_DETECTED) {
                dragging = true;
                dragStartX = event.getSceneX() - element.getLayoutX();
                dragStartY = event.getSceneY() - element.getLayoutY();
                deleteMovePreview();
                preview = createMovePreview();
            }
            else if (event.getEventType() == MouseEvent.MOUSE_DRAGGED) {
                if (preview != null) {
                    double dragEndX = event.getSceneX();
                    double dragEndY = event.getSceneY();
                    // these two lines need to be put into a factory ASAP
                    MoveCommandArgs args = new MoveCommandArgs(
                            dragStartX, dragStartY,
                            dragEndX, dragEndY,
                            preview);
                    MoveCommand command = new MoveCommand(args);
                    command.execute();
                }
            }
            else if (event.getEventType() == MouseEvent.MOUSE_RELEASED) {
                deleteMovePreview();
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

    private ImageView createMovePreview() {
        // create preview
        WritableImage img = element.snapshot(null, new WritableImage(
                (int) element.getBoundsInParent().getWidth(),
                (int) element.getBoundsInParent().getHeight()));

        preview = new ImageView(img);
        preview.setLayoutX(dragStartX);
        preview.setLayoutX(dragStartY);
        preview.setOpacity(0.5);
        ((Pane) element.getParent()).getChildren().add(preview);
        return preview;
    }

    private void deleteMovePreview() {
        if (preview == null) {
            return;
        }
        ((Pane) element.getParent()).getChildren().remove(preview);
        preview = null;
    }
}
