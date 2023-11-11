package carleton.sysc4907.controller;

import carleton.sysc4907.model.Diagram;
import javafx.fxml.FXML;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;

/**
 * Controller for the element library panel, the diagram editor panel which shows new element templates
 * that can be added to the diagram.
 */
public class ElementLibraryPanelController {

    @FXML
    private FlowPane elementsPane;
    @FXML
    private TitledPane titledPane;

    /**
     * Constructs a new ElementLibraryPanelController.
     */
    public ElementLibraryPanelController() {

    }

    /**
     * Initializes the view.
     */
    @FXML
    public void initialize() {

    }
}