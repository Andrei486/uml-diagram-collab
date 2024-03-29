package carleton.sysc4907;

import carleton.sysc4907.controller.FormattingPanelController;
import carleton.sysc4907.controller.SessionInfoBarController;
import carleton.sysc4907.controller.StartScreenController;
import carleton.sysc4907.model.PreferencesModel;
import carleton.sysc4907.model.SessionModel;
import carleton.sysc4907.controller.SessionUsersMenuController;
import carleton.sysc4907.model.*;
import carleton.sysc4907.processing.FileLoader;
import carleton.sysc4907.processing.RoomCodeManager;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import jdk.jshell.Diag;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * The main JavaFX application. Cannot be run directly unless JavaFX is installed or unless using the Maven javafx:run
 * plugin. Use Main.java to run instead.
 */
public class App extends Application {

    //true if the direct connection dialog should be used when joining a diagram
    //Direct connection uses an IP address and port to connection. It is for testing only.
    //Otherwise, the dialog to enter a room code will be used
    final boolean USE_DIRECT_CONNECTION = true;

    /**
     * Starts the application.
     * @param stage the Stage to show the application in
     * @throws IOException if loading the FXML for the application fails
     */
    @Override
    public void start(Stage stage) throws IOException {
        //Create dependency injector to link models and controllers
        DependencyInjector injector = new DependencyInjector();
        PreferencesModel preferencesModel = new PreferencesModel();
        DiagramEditorLoader loader = new DiagramEditorLoader();
        RoomCodeManager roomCodeManager = new RoomCodeManager();
        FileLoader fileLoader = new FileLoader(loader);

        //Add instantiation methods to the dependency injector
        injector.addInjectionMethod(StartScreenController.class, () -> new StartScreenController(
                preferencesModel,
                loader,
                roomCodeManager,
                fileLoader,
                USE_DIRECT_CONNECTION));

        //Set up and show the scene
        Scene scene = new Scene(injector.load("view/StartScreenView.fxml"), 640, 480);
        scene.getStylesheets().add("stylesheet/StartScreenStyle.css");
        scene.getStylesheets().add("stylesheet/DialogBoxStyle.css");
        stage.setScene(scene);
        stage.setTitle("UML Collaboration Tool");
        try {
            stage.getIcons().add(new Image(Objects.requireNonNull(App.class.getResourceAsStream("/icons/app_icon.png"))));
        }
        catch (NullPointerException e) {
            //this means the app icon was unable to load, continue without loading it
        }

        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}