package carleton.sysc4907.controller;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * Controller for the element library panel, the diagram editor panel which shows new element templates
 * that can be added to the diagram.
 */
public class ElementLibraryPanelController {

    @FXML
    private FlowPane elementsPane;
    @FXML
    private TitledPane titledPane;

    private final DiagramModel diagramModel;
    private final DependencyInjector elementInjector;
    private Pane editingArea = null;

    /**
     * Constructs a new ElementLibraryPanelController.
     * @param diagramModel the DiagramModel for the current diagram
     * @param elementInjector the DependencyInjector used to load new diagram elements from FXML
     */
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
        List<Button> buttons = new LinkedList<>();
        buttons.add(createAddButton(
                "Rectangle",
                "/carleton/sysc4907/view/element/Rectangle.fxml"
        ));
        buttons.add(createAddButton(
                "UML Comment",
                "/carleton/sysc4907/view/element/UmlComment.fxml"
        ));
        elementsPane.getChildren().addAll(buttons);
    }

    /**
     * Create a button to add a specific kind of element to the diagram.
     * @param elementName the human-readable name of the element
     * @param fxmlPath the path of the FXML defining the element
     * @return a Button that, when clicked, will instantiate the element and add it to the diagram
     */
    private Button createAddButton(String elementName, String fxmlPath) {
        Button button = new Button(elementName);
        button.setOnAction(actionEvent -> {
            Parent obj;
            try {
                obj = elementInjector.load(fxmlPath);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            editingArea.getChildren().add(obj);
            DiagramElement element = (DiagramElement) obj;
            diagramModel.getElements().add(element);
            diagramModel.getSelectedElements().clear();
            diagramModel.getSelectedElements().add(element);
        });
        return button;
    }
}