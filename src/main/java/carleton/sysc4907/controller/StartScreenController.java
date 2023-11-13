package carleton.sysc4907.controller;

import carleton.sysc4907.model.PreferencesModel;
import carleton.sysc4907.processing.FileLoader;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.beans.value.ObservableValue;

import javax.swing.*;
//import javax.swing.event.ChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.Proxy;

public class StartScreenController {
    public Button openBtn;
    public Button newBtn;
    public Button joinBtn;
    public EnterRoomCodeDialog enterRoomCodeDialog;
    public TextField usernameField;

    private PreferencesModel preferences;


    public StartScreenController(PreferencesModel preferences) {
        this.preferences = preferences;

        //what is this warning?
        /*
        this.preferences.usernameProperty().addListener(new ChangeListener() {
            @Override public void changed(ObservableValue o, Object oldVal, Object newVal) {
                if (newVal instanceof String) {
                    usernameField.setText((String) newVal);
                    System.out.println(newVal);
                }
            }
        });
        */


    }

    @FXML
    public void initialize() {
        preferences.usernameProperty().bindBidirectional(usernameField.textProperty());
        preferences.loadVals();
    }

    @FXML
    private void onJoinBtnClicked(ActionEvent actionEvent) {
        //preferences.usernameProperty().
        Window owner = ((Node) actionEvent.getSource()).getScene().getWindow();
        enterRoomCodeDialog = new EnterRoomCodeDialog(owner);
        enterRoomCodeDialog.showAndWait().ifPresent(room -> usernameField.setText(room)); //open connection (for now just set the username field)
    }

    @FXML
    public void onNewBtnClicked(ActionEvent actionEvent) {
        //create new diagram
        //preferences.printUsername();
    }

    @FXML
    public void onOpenBtnClicked(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open");
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        File file = fileChooser.showOpenDialog(stage); // should handle cancelling

        try {
            FileLoader.open(file);
        } catch (IOException e) {
            Dialog<String> dialog = new Dialog<String>();
            dialog.setTitle("An error has occurred");
            dialog.setContentText("An error has occurred opening the selected file: " + file.getPath());
            ButtonType type = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(type);
            dialog.showAndWait();
        }
    }

}
