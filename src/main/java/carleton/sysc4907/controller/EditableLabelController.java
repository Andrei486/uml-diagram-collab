package carleton.sysc4907.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.TextAlignment;

/**
 * Controller for an editable label. The editable label displays as a normal label, until double-clicked:
 * it then becomes a TextArea while focused, allowing editing.
 */
public class EditableLabelController {

    @FXML
    private Label label;
    @FXML
    private TextArea editableText;

    private final StringProperty textProperty = new SimpleStringProperty();

    /**
     * Constructs a new EditableLabelController.
     */
    public EditableLabelController() {
    }

    /**
     * Initializes the editable label.
     */
    @FXML
    public void initialize() {
        textProperty.bind(label.textProperty());
        editableText.setFocusTraversable(false);
        label.setTextAlignment(TextAlignment.CENTER);
        label.setWrapText(true);
        editableText.setWrapText(true);
        toggleEditable(false);
        editableText.focusedProperty().addListener(((observableValue, oldValue, newValue) -> toggleEditable(newValue)));
        label.onMouseClickedProperty().set(mouseEvent -> {
            if (mouseEvent.getClickCount() == 2) {
                startEditing();
            }
        });
    }

    /**
     * Starts editing this label, making it editable and focusing it.
     */
    public void startEditing() {
        toggleEditable(true);
        editableText.requestFocus();
    }

    /**
     * Gets the text property of this editable label.
     * This text property will only change once the label stops being edited, not on every character entered!
     * @return the StringProperty corresponding to this editable label's text
     */
    public StringProperty getTextProperty() {
        return textProperty;
    }

    /**
     * Gets the current text of this editable label.
     * If the text is currently being edited, this will not account for any changes in the current edit.
     * @return the current text of this editable label
     */
    public String getText() {
        return textProperty.get();
    }

    /**
     * Sets this editable label's text to a specific value.
     * If the text is currently being edited, this will immediately be applied, overwriting the current edit.
     * @param text the new value for the text
     */
    public void setText(String text) {
        label.textProperty().set(text);
        editableText.textProperty().set(text);
    }

    /**
     * Toggles the editable label to an editable or uneditable state.
     * @param editable true if the label should become editable, false if it should become uneditable
     */
    private void toggleEditable(boolean editable) {
        label.setVisible(!editable);
        editableText.setVisible(editable);
        editableText.setDisable(!editable);
        editableText.setEditable(editable);
        if (editable) editableText.selectAll();
        label.setText(editableText.getText());
    }
}
