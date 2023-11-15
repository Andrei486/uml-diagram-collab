package carleton.sysc4907.controller;

import carleton.sysc4907.model.SessionModel;
import javafx.fxml.FXML;
import javafx.scene.text.Text;

/**
 * Controller for the session info bar, the bar at the bottom of the diagram editor page
 * that shows session info such as the room code.
 */
public class SessionInfoBarController {

    @FXML
    private Text roomCode;

    private final SessionModel sessionModel;

    /**
     * Creates a SessionInfoBarController.
     * @param sessionModel the SessionModel for the current session
     */
    public SessionInfoBarController(SessionModel sessionModel) {
        this.sessionModel = sessionModel;
    }

    /**
     * Initializes the view, binding model properties.
     */
    @FXML
    public void initialize() {
        // make a text property for the room code on the session object, subscribe to changed on the property
        // set the initial value here
        roomCode.setText("Room code: " + sessionModel.getRoomCode());
    }
}