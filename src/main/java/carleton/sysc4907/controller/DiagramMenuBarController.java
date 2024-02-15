package carleton.sysc4907.controller;

import carleton.sysc4907.command.Command;
import carleton.sysc4907.command.MoveCommandFactory;
import carleton.sysc4907.command.RemoveCommandFactory;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.FileSaver;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.event.ActionEvent;
import javafx.stage.WindowEvent;

import java.io.File;
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
    private final FileSaver fileSaver;

    /**
     * Constructs a new DiagramMenuBarController.
     * @param diagramModel the DiagramModel for the current diagram.
     */
    public DiagramMenuBarController(DiagramModel diagramModel, RemoveCommandFactory removeCommandFactory, FileSaver fileSaver) {
        this.diagramModel = diagramModel;
        this.removeCommandFactory = removeCommandFactory;
        this.fileSaver = fileSaver;
    }

    /**
     * Closes the application.
     */
    public void closeApplication() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        // Need to send a close request to trigger handlers (including closing TCP) instead of forcing a close
        stage.fireEvent(
                new WindowEvent(
                        stage,
                        WindowEvent.WINDOW_CLOSE_REQUEST
                )
        );
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
     * Saves the diagram to the previously chosen file path if there is one,
     * otherwise prompts the user to choose a file instead.
     */
    @FXML
    public void saveDiagram(ActionEvent event) {
        if (diagramModel.getLoadedFilePath() != null) {
            fileSaver.save();
        } else {
            // Prompt the user for a file if this is a new diagram
            saveDiagramAs(event);
        }
    }

    /**
     * Saves the diagram to a file chosen via file chooser.
     */
    @FXML
    public void saveDiagramAs(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Save Location");
        Stage stage = (Stage) menuBar.getScene().getWindow();
        File saveFile = fileChooser.showSaveDialog(stage);

        if (saveFile == null) {
            return; // No file selected, cancel action
        } else {
            boolean success = fileSaver.saveAs(saveFile.getPath());
            if (!success) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not save");
                alert.setHeaderText("Error: Could not save");
                alert.setContentText("An error has occurred saving to the selected file: " + saveFile.getPath());
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
                alert.showAndWait();
            }
        }
    }

    /**
     * Initializes the view.
     */
    @FXML
    public void initialize() {
        deleteElement.disableProperty().bind(diagramModel.getIsElementSelectedProperty().not());
    }
}