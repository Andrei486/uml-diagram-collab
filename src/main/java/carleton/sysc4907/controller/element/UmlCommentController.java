package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.controller.EditableLabelController;
import carleton.sysc4907.model.DiagramModel;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

/**
 * Controller for a UML comment element which supports editable text.
 */
public class UmlCommentController extends DiagramElementController {

    @FXML
    private EditableLabelController editableLabelController; // Get the editable label's controller, keys off the fx:id

    @FXML
    private Rectangle background;

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
            DiagramModel diagramModel) {
        super(previewCreator, moveCommandFactory, diagramModel);
    }

    /**
     * Initializes the comment, setting a default text.
     */
    @FXML
    public void initialize() {
        super.initialize();
        setText("UML Comment"); // Eventually this should probably allow for a specific text.
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