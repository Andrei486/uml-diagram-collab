package carleton.sysc4907.controller;

import carleton.sysc4907.model.DiagramModel;
import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

/**
 * Controller for the interactive diagram editing area.
 */
public class DiagramEditingAreaController {

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private Pane editingArea;

    private final DiagramModel diagramModel;

    /**
     * Constructs a new DiagramEditingAreaController.
     * @param diagramModel the DiagramModel for the current diagram
     */
    public DiagramEditingAreaController(DiagramModel diagramModel) {
        this.diagramModel = diagramModel;
    }

    /**
     * Gets this controller's associated editing area.
     * @return the Pane serving as the editing area
     */
    public Pane getEditingArea() {
        return editingArea;
    }

    /**
     * Initializes the view and sets the editing pane's default position.
     */
    @FXML
    public void initialize() {
        scrollPane.setHvalue(0); // scroll to far left
        scrollPane.setVvalue(0); // scroll to top
        editingArea.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.isStillSincePress()) {
                diagramModel.getSelectedElements().clear();
            }
        });
    }
}