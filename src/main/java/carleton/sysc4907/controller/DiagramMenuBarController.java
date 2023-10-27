package carleton.sysc4907.controller;

import carleton.sysc4907.model.Diagram;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.WritableImage;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

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