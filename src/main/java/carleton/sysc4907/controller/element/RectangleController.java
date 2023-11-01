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

public class RectangleController extends DiagramElementController {

    @FXML
    private Rectangle rect;

    public RectangleController(MovePreviewCreator previewCreator) {
        super(previewCreator);
    }
}
