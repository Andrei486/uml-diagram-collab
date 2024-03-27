package carleton.sysc4907.controller;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.RemoveCommandFactory;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.processing.FileSaver;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javafx.stage.WindowEvent;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.List;

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
    @FXML
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
    public void saveDiagram() {
        if (diagramModel.getLoadedFilePath() != null) {
            fileSaver.save();
        } else {
            // Prompt the user for a file if this is a new diagram
            saveDiagramAs();
        }
    }

    /**
     * Saves the diagram to a file chosen via file chooser.
     */
    @FXML
    public void saveDiagramAs() {
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

    @FXML
    public void exportToImage() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose Export Location");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"),
            new FileChooser.ExtensionFilter("All Files", "*.*")
        );
        Stage stage = (Stage) menuBar.getScene().getWindow();
        File saveFile = fileChooser.showSaveDialog(stage);

        if (saveFile != null) {  // Make sure that a file was selected
            try {
                saveFile.createNewFile();
                System.out.println(saveFile.getPath());
                System.out.println(EditingAreaProvider.getEditingArea());
                WritableImage image = EditingAreaProvider.getEditingArea().snapshot(new SnapshotParameters(), null);
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", saveFile);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Could not export");
                alert.setHeaderText("Error: Could not export");
                alert.setContentText("An error has occurred saving to the selected file: " + saveFile.getPath());
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
                alert.showAndWait();
                return;
            }
        }
    }

    /**
     * Initializes the view.
     */
    @FXML
    public void initialize() {
        deleteElement.disableProperty().bind(diagramModel.getIsElementSelectedProperty().not());
        menuBar.sceneProperty().addListener((observableValue, scene, newScene) -> {
            if (newScene == null) return;
            var root = newScene.getRoot();
            System.out.println(root);
            root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (keyEvent.getCode() == KeyCode.DELETE && diagramModel.getIsElementSelectedProperty().get()) {
                    System.out.println("DELETE key pressed");
                    deleteSelectedElements();
                }
            });
            var saveKeyCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN);
            var saveAsKeyCombination = new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN, KeyCombination.SHIFT_DOWN);
            root.addEventHandler(KeyEvent.KEY_PRESSED, keyEvent -> {
                if (saveAsKeyCombination.match(keyEvent)) {
                    System.out.println("Ctrl+Shift+S pressed");
                    saveDiagramAs();
                } else if (saveKeyCombination.match(keyEvent)) {
                    System.out.println("Ctrl+S pressed");
                    saveDiagram();
                }
            });
        });
    }
}