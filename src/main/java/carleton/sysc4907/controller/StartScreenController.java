package carleton.sysc4907.controller;

import carleton.sysc4907.DiagramEditorLoader;
import carleton.sysc4907.model.PreferencesModel;
import carleton.sysc4907.processing.FileLoader;
import carleton.sysc4907.processing.RoomCodeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

//import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.IOException;

public class StartScreenController {
    public Button openBtn;
    public Button newBtn;
    public Button joinBtn;
    public EnterRoomCodeDialogController enterRoomCodeDialog;
    public TextField usernameField;

    private String roomCode;

    private final PreferencesModel preferences;

    private final RoomCodeManager roomCodeManager;


    public StartScreenController(PreferencesModel preferences) {
        this.preferences = preferences;
        roomCodeManager = new RoomCodeManager();
    }

    @FXML
    public void initialize() {
        preferences.usernameProperty().bindBidirectional(usernameField.textProperty());
        if (preferences.getUsername().isEmpty()) {
            disableButtons();
        }
        else {
            enableButtons();
        }
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (usernameField.textProperty().isEmpty().get()) {
                disableButtons();
            }
            else {
                enableButtons();
            }
        });
    }

    @FXML
    private void onJoinBtnClicked(ActionEvent actionEvent) {
        Window owner = ((Node) actionEvent.getSource()).getScene().getWindow();
        enterRoomCodeDialog = new EnterRoomCodeDialogController(owner);
        enterRoomCodeDialog.showAndWait().ifPresent(room -> {
            roomCode = room;
            if (roomCodeManager.validateRoomCode(roomCode)) {
                Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                String roomCode = roomCodeManager.getNewRoomCode();
                openEditor(roomCode, preferences.getUsername(), stage);
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid room code");
                alert.setHeaderText("Error: Invalid room code");
                alert.setContentText("The room code you have entered is invalid. Please ensure that it is correct.");
                alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
                alert.showAndWait();
            }
        });
    }

    @FXML
    public void onNewBtnClicked(ActionEvent actionEvent) {
        //create new diagram
        //preferences.printUsername();
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        String roomCode = roomCodeManager.getNewRoomCode();
        openEditor(roomCode, preferences.getUsername(), stage);
    }

    @FXML
    public void onOpenBtnClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage); // should handle cancelling

        try {
            if (file != null) {
                FileLoader.open(file);
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Cannot open file");
            alert.setHeaderText("Error: Cannot open file");
            alert.setContentText("An error has occurred opening the selected file: " + file.getPath());
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    private void openEditor(String roomCode, String username, Stage stage) {
        DiagramEditorLoader loader = new DiagramEditorLoader();
        try {
            loader.load(stage, preferences.getUsername(), roomCode);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Application error");
            alert.setContentText("The application has encountered an error creating a new diagram, please try again.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    private void disableButtons() {
        openBtn.setDisable(true);
        newBtn.setDisable(true);
        joinBtn.setDisable(true);
    }

    private void enableButtons() {
        openBtn.setDisable(false);
        newBtn.setDisable(false);
        joinBtn.setDisable(false);
    }

}
