package carleton.sysc4907.controller;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.controller.element.RectangleController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class DiagramEditingAreaController {

    @FXML
    private Pane editingArea;

    public DiagramEditingAreaController() {

    }

    @FXML
    public void initialize() throws IOException {
        FXMLLoader loader = new FXMLLoader(DiagramEditingAreaController.class.getResource(
                "/carleton/sysc4907/view/element/Rectangle.fxml"));
        loader.setController(new RectangleController());
        Parent obj = loader.load();
        editingArea.getChildren().add(obj);
    }
}
