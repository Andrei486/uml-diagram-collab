package carleton.sysc4907.controller;

import carleton.sysc4907.model.FontOptionsFinder;
import carleton.sysc4907.model.TextFormattingModel;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import carleton.sysc4907.model.Diagram;

public class FormattingPanelController {

    @FXML
    private Spinner<Integer> fontSize;
    @FXML
    private ComboBox<String> fontName;
    @FXML
    private TitledPane titledPane;
    @FXML
    private Button underlineButton;
    @FXML
    private Button italicsButton;
    @FXML
    private Button boldButton;

    private final TextFormattingModel textFormattingModel;

    public FormattingPanelController(TextFormattingModel textFormattingModel) {
        this.textFormattingModel = textFormattingModel;
    }

    @FXML
    public void initialize() {
        // deselect and disable all formatting options
        boldButton.setDisable(true);
        italicsButton.setDisable(true);
        underlineButton.setDisable(true);

        fontName.setItems(textFormattingModel.getFontFamilyNames());
    }

}