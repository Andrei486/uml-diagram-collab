package carleton.sysc4907.controller;

import carleton.sysc4907.DiagramEditorLoader;
import carleton.sysc4907.model.PreferencesModel;
import carleton.sysc4907.processing.FileLoader;
import carleton.sysc4907.processing.IpPortParser;
import carleton.sysc4907.processing.RoomCodeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
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

    public DirectConnectionDialogController directConnectDialog;

    public TextField usernameField;

    private String roomCode;

    private String ipPort;

    private final PreferencesModel preferences;

    private final RoomCodeManager roomCodeManager;

    private final IpPortParser ipPortParser;

    private final DiagramEditorLoader loader;

    private final boolean useDirectConnection;

    private String directConnectIp;

    private String directConnectPort;


    public StartScreenController(PreferencesModel preferences, DiagramEditorLoader loader, RoomCodeManager manager, boolean useDirectConnection) {
        this.preferences = preferences;
        this.loader = loader;
        this.useDirectConnection = useDirectConnection;
        roomCodeManager = manager;
        ipPortParser = new IpPortParser();
    }

    @FXML
    public void initialize() {
        preferences.usernameProperty().bindBidirectional(usernameField.textProperty());
        if (preferences.getUsername().trim().isEmpty()) {
            disableButtons();
        }
        else {
            enableButtons();
        }
        usernameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (usernameField.getText().trim().isEmpty()) {
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
        if(!useDirectConnection) {
            enterRoomCodeDialog = new EnterRoomCodeDialogController(owner);
            enterRoomCodeDialog.showAndWait().ifPresent(room -> {
                roomCode = room;
                if (roomCodeManager.validateRoomCode(roomCode)) {
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
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
        else {
            directConnectDialog = new DirectConnectionDialogController(owner);
            directConnectDialog.showAndWait().ifPresent(address -> {
                ipPort = address;
                try {
                    //TODO:
                    //use these later in this method to call a class that will make the connection
                    directConnectIp = ipPortParser.getIp(ipPort);
                    directConnectPort = ipPortParser.getPort(ipPort);

                    //for now, just open the editor with a random room code
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    openEditor(roomCodeManager.getNewRoomCode(), preferences.getUsername(), stage);
                }
                catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Invalid address");
                    alert.setHeaderText("Error: Invalid format");
                    alert.setContentText("The address you entered is not formatted correctly. The format is 'IP:PORT'");
                    alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
                    alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
                    alert.showAndWait();
                }
            });
        }

    }

    @FXML
    public void onNewBtnClicked(ActionEvent actionEvent) {
        //create new diagram
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        String roomCode = roomCodeManager.getNewRoomCode();
        openEditor(roomCode, preferences.getUsername(), stage);
    }

    @FXML
    public void onOpenBtnClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage);

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
