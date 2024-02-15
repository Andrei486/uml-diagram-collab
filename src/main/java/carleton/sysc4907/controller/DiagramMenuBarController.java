package carleton.sysc4907.controller;

import carleton.sysc4907.command.Command;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.RemoveCommandFactory;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.fxml.FXML;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.util.List;
import java.util.function.Consumer;

/**
 * Controller for the main menu bar of the diagram editor, which includes File/Edit options, etc.
 */
public class DiagramMenuBarController {

    @FXML
    private MenuItem deleteElement;

    @FXML
    private MenuBar menuBar;

    private final DiagramModel diagramModel;

    private final RemoveCommandFactory removeCommandFactory;
    private final ExecutedCommandList executedCommandList;

    /**
     * Constructs a new DiagramMenuBarController.
     * @param diagramModel the DiagramModel for the current diagram.
     */
    public DiagramMenuBarController(DiagramModel diagramModel, RemoveCommandFactory removeCommandFactory, ExecutedCommandList executedCommandList) {
        this.diagramModel = diagramModel;
        this.removeCommandFactory = removeCommandFactory;
        this.executedCommandList = executedCommandList;
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
        List<Long> toDelete = diagramModel.getSelectedElements().stream().map(DiagramElement::getElementId).toList();
        if (!toDelete.isEmpty()) {
            RemoveCommandArgs args = new RemoveCommandArgs(toDelete.stream().mapToLong(l -> l).toArray());
            var command = removeCommandFactory.createTracked(args);
            command.execute();
        }
    }

    /**
     * Saves the diagram.
     */
    public void saveDiagram() {
        var commandList = executedCommandList.getCommandList();
        commandList.forEach(System.out::println);
        System.out.println(commandList.size());
    }

    /**
     * Initializes the view.
     */
    @FXML
    public void initialize() {
        deleteElement.disableProperty().bind(diagramModel.getIsElementSelectedProperty().not());
    }
}