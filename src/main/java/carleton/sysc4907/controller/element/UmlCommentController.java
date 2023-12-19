package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.ResizeCommandFactory;
import carleton.sysc4907.model.DiagramModel;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

/**
 * Controller for a UML comment element which supports editable text.
 */
public class UmlCommentController extends ResizableElementController {

    @FXML
    private EditableLabelController editableLabelController; // Get the editable label's controller, keys off the fx:id

    @FXML
    private Rectangle background;

    @FXML
    private StackPane stackPane;

    /**
     * Constructs a new UmlCommentController.
     *
     * @param previewCreator     a MovePreviewCreator, used to create the move preview
     * @param moveCommandFactory a MoveCommandFactory, used to create move commands
     * @param diagramModel       the DiagramModel for the current diagram
     */
    public UmlCommentController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel,
            ResizeHandleCreator resizeHandleCreator,
            ResizeCommandFactory resizeCommandFactory) {
        super(previewCreator, moveCommandFactory, diagramModel, resizeHandleCreator, resizeCommandFactory);
    }

    /**
     * Initializes the comment, setting a default text.
     */
    @FXML
    public void initialize() {
        super.initialize();
        setText("UML Comment"); // Eventually this should probably allow for a specific text.
        background.widthProperty().bind(element.maxWidthProperty());
        background.heightProperty().bind(element.maxHeightProperty());
        stackPane.maxWidthProperty().bind(element.maxWidthProperty());
        stackPane.maxHeightProperty().bind(element.maxHeightProperty());
        editableLabelController.getHeightProperty().bind(element.maxHeightProperty());
        editableLabelController.getWidthProperty().bind(element.maxWidthProperty());
    }

    /**
     * Gets the property corresponding to the UML comment's text.
     * @return the StringProperty for this UML comment's text
     */
    public StringProperty getTextProperty() {
        return editableLabelController.getTextProperty();
    }

    /**
     * Gets the UML comment's label text.
     * @return the current label text
     */
    public String getText() {
        return editableLabelController.getText();
    }

    /**
     * Sets the UML comment's label text directly.
     * @param text the new value for the text
     */
    public void setText(String text) {
        editableLabelController.setText(text);
    }
}