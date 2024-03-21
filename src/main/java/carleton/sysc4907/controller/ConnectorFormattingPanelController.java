package carleton.sysc4907.controller;

import carleton.sysc4907.command.ChangeConnectorStyleCommandFactory;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.ChangeConnectorStyleCommandArgs;
import carleton.sysc4907.controller.element.ArrowConnectorElementController;
import carleton.sysc4907.controller.element.arrows.ConnectorType;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

public class ConnectorFormattingPanelController {

    private final String MULTIPLE_SELECTED_TYPES = "";
    private boolean autoUpdatingComboBox = false;

    @FXML
    private ComboBox<String> connectorStyle;

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
        connectorStyle.getItems().addAll(Arrays.stream(ConnectorType.values()).map(Enum::toString).toList());
        connectorStyle.setValue(MULTIPLE_SELECTED_TYPES);
        diagramModel.getSelectedElements().addListener((ListChangeListener<DiagramElement>) change -> {
            boolean arrowSelected = false;
            var selectedArrowTypes = new HashSet<ConnectorType>();
            var selectedElements = diagramModel.getSelectedElements();
            for (var element : selectedElements) {
                var controller = elementIdManager.getElementControllerById(element.getElementId());
                if (controller instanceof ArrowConnectorElementController arrowController) {
                    arrowSelected = true;
                    selectedArrowTypes.add(arrowController.getConnectorType());
                }
            }
            connectorStyle.setDisable(!arrowSelected);
            boolean wasAutoUpdating = autoUpdatingComboBox;
            autoUpdatingComboBox = true;
            if (selectedArrowTypes.size() != 1) {
                connectorStyle.setValue(MULTIPLE_SELECTED_TYPES);
            } else {
                connectorStyle.setValue(selectedArrowTypes.stream().toList().get(0).toString());
            }
            autoUpdatingComboBox = wasAutoUpdating;
        });
        connectorStyle.valueProperty().addListener((observableValue, oldValue, newValue) -> {
            if (autoUpdatingComboBox || Objects.equals(newValue, MULTIPLE_SELECTED_TYPES)) {
                return;
            }
            var selectedElements = diagramModel.getSelectedElements();
            for (var element : selectedElements) {
                var elementId = element.getElementId();
                var controller = elementIdManager.getElementControllerById(elementId);
                if (controller instanceof ArrowConnectorElementController arrowConnectorElementController) {
                    var args = new ChangeConnectorStyleCommandArgs(elementId, ConnectorType.valueOf(newValue));
                    var command = changeConnectorStyleCommandFactory.createTracked(args);
                    command.execute();
                }
            }
        });
    }
}
