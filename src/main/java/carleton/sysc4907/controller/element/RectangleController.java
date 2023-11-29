package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.model.DiagramModel;
import javafx.fxml.FXML;
import javafx.scene.shape.Rectangle;

public class RectangleController extends ResizableElementController {

    @FXML
    private Rectangle rect;

    public RectangleController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel,
            ResizeHandleCreator resizeHandleCreator) {
        super(previewCreator, moveCommandFactory, diagramModel, resizeHandleCreator);
    }

    @Override
    public void initialize() {
        super.initialize();
        rect.widthProperty().bind(element.maxWidthProperty());
        rect.heightProperty().bind(element.maxHeightProperty());
    }
}