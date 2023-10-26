package carleton.sysc4907;

import carleton.sysc4907.model.SessionModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HelloFX extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        DependencyInjector injector = new DependencyInjector();
        SessionModel sessionModel = new SessionModel();
        Startup.registerInstance(SessionModel.class, sessionModel);
        Startup.registerControllerInjectionMethods(injector);

        Scene scene = new Scene(injector.load("view/SessionInfoBar.fxml"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}