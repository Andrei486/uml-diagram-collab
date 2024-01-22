package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.ResizeCommandFactory;
import carleton.sysc4907.model.DiagramModel;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

public class UmlClassController extends ResizableElementController{
    @FXML
    public Rectangle titleRectangle;
    @FXML
    public Rectangle fieldsRectangle;
    @FXML
    public Group fieldsLabel;
    @FXML
    public Rectangle methodsRectangle;
    @FXML
    public Group methodsLabel;
    @FXML
    private EditableLabelController editableLabelController;

    @FXML
    private Rectangle background;

    @FXML
    private StackPane bgStackPane;

    /**
     * Constructs a new UmlCommentController.
     *
     * @param previewCreator     a MovePreviewCreator, used to create the move preview
     * @param moveCommandFactory a MoveCommandFactory, used to create move commands
     * @param diagramModel       the DiagramModel for the current diagram
     */
    public UmlClassController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel,
            ResizeHandleCreator resizeHandleCreator,
            ResizePreviewCreator resizePreviewCreator,
            ResizeCommandFactory resizeCommandFactory) {
        super(previewCreator, moveCommandFactory, diagramModel, resizeHandleCreator, resizePreviewCreator, resizeCommandFactory);
    }

    /**
     * Initializes the comment, setting a default text.
     */
    @FXML
    public void initialize() {
        super.initialize();
        background.widthProperty().bind(element.maxWidthProperty());
        background.heightProperty().bind(element.maxHeightProperty());
        bgStackPane.maxWidthProperty().bind(element.maxWidthProperty());
        bgStackPane.maxHeightProperty().bind(element.maxHeightProperty());
        editableLabelController.getHeightProperty().bind(element.maxHeightProperty());
        editableLabelController.getWidthProperty().bind(element.maxWidthProperty());
        setText("UML Class");
    }

    public StringProperty getTextProperty() {
        return editableLabelController.getTextProperty();
    }

    public String getText() {
        return editableLabelController.getText();
    }

    public void setText(String text) {
        editableLabelController.setText(text);
    }
}


