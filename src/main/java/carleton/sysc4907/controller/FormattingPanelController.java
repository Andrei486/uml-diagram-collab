package carleton.sysc4907.controller;

import carleton.sysc4907.model.TextFormattingModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;

/**
 * Controller for the formatting panel, the panel on the diagram editor page that provides text formatting options.
 */
public class FormattingPanelController {

    @FXML
    private Spinner<Integer> fontSize;
    @FXML
    private ComboBox<String> fontName;
    @FXML
    private TitledPane titledPane;
    @FXML
    private ToggleButton underlineButton;
    @FXML
    private ToggleButton italicsButton;
    @FXML
    private ToggleButton boldButton;

    private final TextFormattingModel textFormattingModel;

    /**
     * Constructs a FormattingPanelController.
     * @param textFormattingModel the TextFormattingModel containing text formatting information
     */
    public FormattingPanelController(TextFormattingModel textFormattingModel) {
        this.textFormattingModel = textFormattingModel;
    }

    /**
     * Initializes the view.
     */
    @FXML
    public void initialize() {
        // deselect and disable all formatting options
        fontName.setItems(textFormattingModel.getFontFamilyNames());
    }

}