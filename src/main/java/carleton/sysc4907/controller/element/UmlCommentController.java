package carleton.sysc4907.controller.element;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.model.DiagramModel;
import javafx.css.PseudoClass;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;

public class UmlCommentController extends DiagramElementController {

    @FXML
    private Label label;
    @FXML
    private TextArea editableText;
    @FXML
    private Rectangle background;

    public UmlCommentController(
            MovePreviewCreator previewCreator,
            MoveCommandFactory moveCommandFactory,
            DiagramModel diagramModel) {
        super(previewCreator, moveCommandFactory, diagramModel);
        addMouseHandler(MouseEvent.MOUSE_CLICKED, this::handleDoubleClickEditText);
    }

    @FXML
    public void initialize() {
        super.initialize();
        editableText.setFocusTraversable(false);
        label.setTextAlignment(TextAlignment.CENTER);
        label.textProperty().bind(editableText.textProperty());
        label.setWrapText(true);
        editableText.setWrapText(true);
        toggleEditable(false);
        editableText.focusedProperty().addListener(((observableValue, oldValue, newValue) -> toggleEditable(newValue)));
    }

    private void handleDoubleClickEditText(MouseEvent event) {
        if (event.getClickCount() == 2) {
            System.out.println("Text element double clicked!");
            toggleEditable(true);
            editableText.requestFocus();
        }
    }

    private void toggleEditable(boolean editable) {
        label.setVisible(!editable);
        editableText.setVisible(editable);
        editableText.setDisable(!editable);
        editableText.setEditable(editable);
    }
}