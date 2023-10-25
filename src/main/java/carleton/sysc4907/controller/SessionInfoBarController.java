package carleton.sysc4907.controller;

import carleton.sysc4907.model.Diagram;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

public class SessionInfoBarController {


    @FXML
    private Text roomCode;
    private final Diagram diagram;

    public SessionInfoBarController() {
        diagram = Diagram.getSingleInstance();
    }

    @FXML
    public void initialize() {
        // make a text property for the room code on the session object, subscribe to changed on the property
        // set the initial value here
        roomCode.setText("Room code: 123456789012");
    }
}
