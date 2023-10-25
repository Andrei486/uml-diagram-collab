package carleton.sysc4907.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import carleton.sysc4907.model.Diagram;

public class FormattingPanelController {

    @FXML
    private TitledPane titledPane;
    @FXML
    private Button underlineButton;
    @FXML
    private Button italicsButton;
    @FXML
    private Button boldButton;
    private final Diagram diagram;

    public FormattingPanelController() {
        diagram = Diagram.getSingleInstance();
    }

    @FXML
    public void initialize() {
        // deselect and disable all formatting options
        boldButton.setDisable(true);
        italicsButton.setDisable(true);
        underlineButton.setDisable(true);
    }

}
