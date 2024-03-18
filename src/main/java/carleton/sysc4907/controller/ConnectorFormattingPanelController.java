package carleton.sysc4907.controller;

import carleton.sysc4907.command.ChangeConnectorStyleCommandFactory;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.ChangeConnectorStyleCommandArgs;
import carleton.sysc4907.controller.element.ArrowConnectorElementController;
import carleton.sysc4907.controller.element.arrows.ConnectorType;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.List;

public class ConnectorFormattingPanelController {

    @FXML
    private VBox connectorStyleContainer;

    private final DiagramModel diagramModel;
    private final ElementIdManager elementIdManager;
    private final ChangeConnectorStyleCommandFactory changeConnectorStyleCommandFactory;

    public ConnectorFormattingPanelController(
            DiagramModel diagramModel,
            ElementIdManager elementIdManager,
            ChangeConnectorStyleCommandFactory changeConnectorStyleCommandFactory) {
        this.diagramModel = diagramModel;
        this.elementIdManager = elementIdManager;
        this.changeConnectorStyleCommandFactory = changeConnectorStyleCommandFactory;
    }

    @FXML
    public void initialize() {
        List<Button> buttons = new LinkedList<>();
        for (ConnectorType type : ConnectorType.values()) {
            buttons.add(createStyleButton(type));
        }
        connectorStyleContainer.getChildren().addAll(buttons);
        diagramModel.getSelectedElements().addListener(new ListChangeListener<DiagramElement>() {
            @Override
            public void onChanged(Change<? extends DiagramElement> change) {
                boolean arrowSelected = false;
                var selectedElements = diagramModel.getSelectedElements();
                for (var element : selectedElements) {
                    var controller = elementIdManager.getElementControllerById(element.getElementId());
                    if (controller instanceof ArrowConnectorElementController) {
                        arrowSelected = true;
                        break;
                    }
                }
                connectorStyleContainer.setDisable(!arrowSelected);
            }
        });
    }

    private Button createStyleButton(ConnectorType type) {
        Button button = new Button(type.toString());
        button.setOnAction(actionEvent -> {
            var selectedElements = diagramModel.getSelectedElements();
            for (var element : selectedElements) {
                var elementId = element.getElementId();
                var controller = elementIdManager.getElementControllerById(elementId);
                if (controller instanceof ArrowConnectorElementController arrowConnectorElementController) {
                    var args = new ChangeConnectorStyleCommandArgs(elementId, type);
                    var command = changeConnectorStyleCommandFactory.createTracked(args);
                    command.execute();
                }
            }
        });
        return button;
    }
}
