package carleton.sysc4907.controller;

import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.RemoveCommandFactory;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;

/**
 * Controller for the main menu bar of the diagram editor, which includes File/Edit options, etc.
 */
public class DiagramMenuBarController {

    @FXML
    private MenuItem deleteElement;

    @FXML
    private MenuBar menuBar;

    private Pane editingArea;
    private final DiagramModel diagramModel;

    private final RemoveCommandFactory removeCommandFactory;

    /**
     * Constructs a new DiagramMenuBarController.
     * @param diagramModel the DiagramModel for the current diagram.
     */
    public DiagramMenuBarController(DiagramModel diagramModel, RemoveCommandFactory removeCommandFactory) {
        this.diagramModel = diagramModel;
        this.removeCommandFactory = removeCommandFactory;
    }

    /**
     * Sets the editing area where users can manipulate elements.
     * Should be called on initialization.
     * @param editingArea the new editing area
     */
    public void setEditingArea(Pane editingArea) {
        this.editingArea = editingArea;
    }

    /**
     * Closes the application.
     */
    public void closeApplication() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        stage.close();
    }

    /**
     * Deletes all selected elements from the diagram.
     */
    public void deleteSelectedElements() {
        List<DiagramElement> toDelete = diagramModel.getSelectedElements();
        RemoveCommandArgs args = new RemoveCommandArgs(toDelete);
        var command = removeCommandFactory.create(args);
        command.execute();
    }
    /*
    public void deleteSelectedElements() {
        List<DiagramElement> toDelete = diagramModel.getSelectedElements();
        System.out.println("Selected element: " + toDelete);
        editingArea.getChildren().removeAll(toDelete);
        diagramModel.getElements().removeAll(toDelete);
        diagramModel.getSelectedElements().clear();
    }
     */

    /**
     * Initializes the view.
     */
    @FXML
    public void initialize() {
        deleteElement.disableProperty().bind(diagramModel.getIsElementSelectedProperty().not());
    }
}