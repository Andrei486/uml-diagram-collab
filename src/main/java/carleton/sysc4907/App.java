package carleton.sysc4907;

import carleton.sysc4907.controller.FormattingPanelController;
import carleton.sysc4907.controller.SessionInfoBarController;
import carleton.sysc4907.controller.SessionUsersMenuController;
import carleton.sysc4907.model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jdk.jshell.Diag;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 * The main JavaFX application. Cannot be run directly unless JavaFX is installed or unless using the Maven javafx:run
 * plugin. Use Main.java to run instead.
 */
public class App extends Application {

    /**
     * Starts the application.
     * @param stage the Stage to show the application in
     * @throws IOException if loading the FXML for the application fails
     */
    @Override
    public void start(Stage stage) throws IOException {
        DiagramEditorLoader loader = new DiagramEditorLoader();
        loader.load(stage);
    }

    public static void main(String[] args) {
        launch();
    }

}