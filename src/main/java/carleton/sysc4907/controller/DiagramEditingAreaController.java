package carleton.sysc4907.controller;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.controller.element.MovePreviewCreator;
import carleton.sysc4907.controller.element.RectangleController;
import carleton.sysc4907.model.DiagramModel;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.io.IOException;

public class DiagramEditingAreaController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane editingArea;

    private final DiagramModel diagramModel;

    public DiagramEditingAreaController(DiagramModel diagramModel) {
        this.diagramModel = diagramModel;
    }

    public Pane getEditingArea() {
        return editingArea;
    }

    @FXML
    public void initialize() {
        scrollPane.setHvalue(0.5);
        scrollPane.setVvalue(0.5);
        editingArea.setOnMouseClicked(mouseEvent -> diagramModel.getSelectedElements().clear());
    }
}
