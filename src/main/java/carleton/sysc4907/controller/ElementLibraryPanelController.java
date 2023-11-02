package carleton.sysc4907.controller;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.controller.element.MovePreviewCreator;
import carleton.sysc4907.controller.element.RectangleController;
import carleton.sysc4907.model.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ElementLibraryPanelController {

    @FXML
    private FlowPane elementsPane;
    @FXML
    private TitledPane titledPane;

    private final DiagramModel diagramModel;
    private final DependencyInjector elementInjector;
    private Pane editingArea = null;

    private double centerXPosition = 0;
    private double centerYPosition = 0;

    public ElementLibraryPanelController(DiagramModel diagramModel, DependencyInjector elementInjector) {
        this.diagramModel = diagramModel;
        this.elementInjector = elementInjector;
    }

    /**
     * Set the Pane to be used as the editing area when adding elements.
     * @param editingArea the Pane to add elements to
     */
    public void setEditingArea(Pane editingArea) {
        this.editingArea = editingArea;
    }

    @FXML
    public void initialize() {
        Button rectangleButton = new Button("Rectangle");
        rectangleButton.setOnAction(actionEvent -> {
            Parent obj;
            try {
                obj = elementInjector.load("/carleton/sysc4907/view/element/Rectangle.fxml");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            obj.setLayoutX(1550);
            obj.setLayoutY(1600);
            editingArea.getChildren().add(obj);
            DiagramElement element = (DiagramElement) obj;
            diagramModel.getElements().add(element);
            diagramModel.getSelectedElements().clear();
            diagramModel.getSelectedElements().add(element);
        });
        elementsPane.getChildren().add(rectangleButton);
    }
}