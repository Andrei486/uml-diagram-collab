package carleton.sysc4907.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.stage.Stage;


public class DiagramMenuBarController {

    @FXML
    private MenuBar menuBar;

    public DiagramMenuBarController() {
    }

    public void closeApplication(ActionEvent actionEvent) {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }
}