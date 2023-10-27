package carleton.sysc4907;

import carleton.sysc4907.controller.SessionInfoBarController;
import carleton.sysc4907.model.SessionModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //Create dependency injector to link models and controllers
        DependencyInjector injector = new DependencyInjector();
        //Create the session
        SessionModel sessionModel = new SessionModel();
        //Add instantiation methods to the dependency injector
        injector.addInjectionMethod(SessionInfoBarController.class, () -> new SessionInfoBarController(sessionModel));

        //Set up and show the scene
        Scene scene = new Scene(injector.load("view/StartScreenView.fxml"), 640, 480);
        scene.getStylesheets().add("stylesheet/StartScreenStyle.css");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}