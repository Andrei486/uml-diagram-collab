package carleton.sysc4907.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;

/**
 * Controller for the main menu bar of the diagram editor, which includes File/Edit options, etc.
 */
public class DiagramMenuBarController {

    @FXML
    private MenuBar menuBar;

    /**
     * Constructs a DiagramMenuBarController.
     */
    public DiagramMenuBarController() {
    }

    /**
     * Closes the application.
     * @param actionEvent the triggering ActionEvent
     */
    public void closeApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }
}