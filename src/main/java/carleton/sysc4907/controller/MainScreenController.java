package carleton.sysc4907.controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;

public class MainScreenController {
    public ScrollPane imageDisplay;

    @FXML
    private MenuController menuController;

    @FXML
    public void initialize() {
        menuController.setExportScrollPane(imageDisplay);
    }
}
