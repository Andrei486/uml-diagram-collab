package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.model.DiagramModel;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

public class RectangleController extends DiagramElementController {

    @FXML
    private Rectangle background;

    public RectangleController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel) {
        super(previewCreator, moveCommandFactory, diagramModel);
    }
}