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
    public void resize(
            boolean isTopAnchor,
            boolean isRightAnchor,
            double dragStartX,
            double dragStartY,
            double dragEndX,
            double dragEndY) {
        double widthChange = dragEndX - dragStartX;
        widthChange = isRightAnchor ? widthChange : -widthChange;
        double heightChange = dragEndY - dragStartY;
        heightChange = isTopAnchor ? -heightChange : heightChange;
        rect.setWidth(rect.getWidth() + widthChange);
        rect.setHeight(rect.getHeight() + heightChange);
    }
}