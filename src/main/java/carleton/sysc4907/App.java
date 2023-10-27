package carleton.sysc4907;

import carleton.sysc4907.controller.FormattingPanelController;
import carleton.sysc4907.controller.SessionInfoBarController;
import carleton.sysc4907.model.FontOptionsFinder;
import carleton.sysc4907.model.SessionModel;
import carleton.sysc4907.model.TextFormattingModel;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        //Create dependency injector to link models and controllers
        DependencyInjector injector = new DependencyInjector();
        //Create the models and supporting classes
        FontOptionsFinder fontOptionsFinder = new FontOptionsFinder();
        SessionModel sessionModel = new SessionModel();
        TextFormattingModel textFormattingModel = new TextFormattingModel(fontOptionsFinder);
        //Add instantiation methods to the dependency injector
        injector.addInjectionMethod(SessionInfoBarController.class,
                () -> new SessionInfoBarController(sessionModel));
        injector.addInjectionMethod(FormattingPanelController.class,
                () -> new FormattingPanelController(textFormattingModel));

        //Set up and show the scene
        Scene scene = new Scene(injector.load("view/DiagramEditorScreen.fxml"), 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Diagram Editor");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }

}