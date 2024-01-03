package carleton.sysc4907.controller;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.AddCommandFactory;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.processing.ElementCreator;
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

    private final AddCommandFactory addCommandFactory;
    @FXML
    private FlowPane elementsPane;
    @FXML
    private TitledPane titledPane;

    private final DiagramModel diagramModel;

    private final ElementCreator elementCreator;

    /**
     * Constructs a new ElementLibraryPanelController.
     * @param diagramModel the DiagramModel for the current diagram
     */
    public ElementLibraryPanelController(
            DiagramModel diagramModel,
            AddCommandFactory addCommandFactory,
            ElementCreator elementCreator) {
        this.diagramModel = diagramModel;
        this.addCommandFactory = addCommandFactory;
        this.elementCreator = elementCreator;
    }

    @FXML
    public void initialize() {
        List<Button> buttons = new LinkedList<>();
        for (String type : elementCreator.getRegisteredTypes()) {
            buttons.add(createAddButton(type));
        }
        elementsPane.getChildren().addAll(buttons);
    }

    /**
     * Create a button to add a specific kind of element to the diagram.
     * @param elementName the name of the element, recognizable by the ElementCreator
     * @return a Button that, when clicked, will instantiate the element and add it to the diagram
     */
    private Button createAddButton(String elementName) {
        Button button = new Button(elementName);
        button.setOnAction(actionEvent -> {
            AddCommandArgs args = new AddCommandArgs(elementName);
            var command = addCommandFactory.create(args);
            command.execute();

        });
        return button;
    }
}