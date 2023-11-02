package carleton.sysc4907.controller;

import carleton.sysc4907.model.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.beans.property.BooleanProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;


public class DiagramMenuBarController {

    @FXML
    private MenuItem deleteElement;

    @FXML
    private MenuBar menuBar;

    private Pane editingArea;
    private final DiagramModel diagramModel;

    public DiagramMenuBarController(DiagramModel diagramModel) {
        this.diagramModel = diagramModel;
    }

    public void setEditingArea(Pane editingArea) {
        this.editingArea = editingArea;
    }

    public void closeApplication() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }

    public void deleteElement() {
        List<DiagramElement> toDelete = diagramModel.getSelectedElements();
        System.out.println("Selected element: " + toDelete);
        editingArea.getChildren().removeAll(toDelete);
        diagramModel.getElements().removeAll(toDelete);
        diagramModel.getSelectedElements().clear();
    }

    @FXML
    public void initialize() {
        deleteElement.disableProperty().bind(diagramModel.getIsElementSelectedProperty());
    }
}