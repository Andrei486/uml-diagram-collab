package carleton.sysc4907.controller;

import carleton.sysc4907.App;
import carleton.sysc4907.DiagramEditorLoader;
import carleton.sysc4907.model.PreferencesModel;
import carleton.sysc4907.processing.FileLoader;
import carleton.sysc4907.processing.IpPortParser;
import carleton.sysc4907.processing.RoomCodeManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

//import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Objects;

/**
 * Controller for the start screen
 */
public class StartScreenController {
    public Button openBtn;
    public Button newBtn;
    public Button joinBtn;
    public EnterRoomCodeDialogController enterRoomCodeDialog;

    public DirectConnectionDialogController directConnectDialog;

    public TextField usernameField;

    public ImageView iconIV;

    private String roomCode;

    private String ipPort;

    private final PreferencesModel preferences;

    private final RoomCodeManager roomCodeManager;

    private final IpPortParser ipPortParser;

    private final DiagramEditorLoader loader;
    private final FileLoader fileLoader;

    private final boolean useDirectConnection;

    private String directConnectIp;

    private String directConnectPort;


    public StartScreenController(
            PreferencesModel preferences,
            DiagramEditorLoader loader,
            RoomCodeManager manager,
            FileLoader fileLoader,
            boolean useDirectConnection) {
        this.preferences = preferences;
        this.loader = loader;
        this.fileLoader = fileLoader;
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
        try {
            Image img = new Image(Objects.requireNonNull(App.class.getResourceAsStream("/icons/app_icon_495x495.png")));
            iconIV.setImage(img);
        }
        catch (NullPointerException e) {
            //this means the app icon was unable to load, continue without loading it
        }

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
                    //parse IP and port
                    directConnectIp = ipPortParser.getIp(ipPort);
                    directConnectPort = ipPortParser.getPort(ipPort);

                    //connect
                    Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
                    joinEditor(directConnectIp, Integer.parseInt(directConnectPort), preferences.getUsername(), stage);
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
                String roomCode = roomCodeManager.getNewRoomCode();
                fileLoader.open(file, roomCode, preferences.getUsername(), stage);
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

    /**
     * Open a new diagram with a room code.
     * @param roomCode the room code to use
     * @param username the username to use
     * @param stage the application stage
     */
    private void openEditor(String roomCode, String username, Stage stage) {
        try {
            loader.createAndLoad(stage, preferences.getUsername(), roomCode);
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

    /**
     * Attempt to join a room via direct connection and open the editor.
     * @param host the host user's IP
     * @param port the port to connect to
     * @param username the username to use
     * @param stage the application stage
     */
    private void joinEditor(String host, int port, String username, Stage stage) {
        try {
            loader.loadJoin(stage, preferences.getUsername(), host, port, new Object[0]);
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Join error");
            alert.setContentText("The application has encountered an error joining the diagram, " +
                    "please ensure the information entered is correct and try again.");
            alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
            alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);
            alert.showAndWait();
        }
    }

    /**
     * Disables the open, new, and join buttons
     */
    private void disableButtons() {
        openBtn.setDisable(true);
        newBtn.setDisable(true);
        joinBtn.setDisable(true);
    }

    /**
     * Enables the open, new, and join buttons
     */
    private void enableButtons() {
        openBtn.setDisable(false);
        newBtn.setDisable(false);
        joinBtn.setDisable(false);
    }

    /* TODO move this code to where the messages are sent and received
    public void onTestBtnClicked(ActionEvent actionEvent) {
        Window owner = ((Node) actionEvent.getSource()).getScene().getWindow();
        JoinRequestDialogController joinRequestDialogController = new JoinRequestDialogController(owner, "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        var result = joinRequestDialogController.showAndWait();
        System.out.println("join dialog result: "+result);
    }
     */
}
