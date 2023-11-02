package carleton.sysc4907.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;

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

    @FXML
    public void initialize() {
        elementLibraryPanelController.setEditingArea(diagramEditingAreaController.getEditingArea());
        diagramMenuController.setEditingArea(diagramEditingAreaController.getEditingArea());
    }
}
