package carleton.sysc4907.controller;

import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;

/**
 * Controller for the diagram editor screen as a whole.
 */
public class DiagramEditorScreenController {

    @FXML
    private MenuBar usersMenu;

    @FXML
    private MenuBar diagramMenu;

    @FXML
    private ScrollPane diagramEditingArea;
    @FXML
    private DiagramMenuBarController diagramMenuController;

    @FXML
    private ElementLibraryPanelController elementLibraryPanelController;

    @FXML
    private DiagramEditingAreaController diagramEditingAreaController;

    /**
     * Initializes the view, updating references to the editing area.
     */
    @FXML
    public void initialize() {
        elementLibraryPanelController.setEditingArea(diagramEditingAreaController.getEditingArea());
        diagramMenuController.setEditingArea(diagramEditingAreaController.getEditingArea());
    }
}