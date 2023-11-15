package carleton.sysc4907.view;
import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.controller.StartScreenController;
import carleton.sysc4907.model.PreferencesModel;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
public class StartScreenNewDiagramTest {

    @Start
    private void start(Stage stage) throws IOException {
        //Create dependency injector to link models and controllers
        DependencyInjector injector = new DependencyInjector();
        PreferencesModel preferencesModel = new PreferencesModel();

        //Add instantiation methods to the dependency injector
        injector.addInjectionMethod(StartScreenController.class, () -> new StartScreenController(preferencesModel));

        //Set up and show the scene
        Scene scene = new Scene(injector.load("view/StartScreenView.fxml"), 640, 480);
        scene.getStylesheets().add("stylesheet/StartScreenStyle.css");
        stage.setScene(scene);
        stage.setTitle("Collaborative UML Diagram Tool");
        stage.show();
    }

    /*
    @Test
    public void newBtnTest(FxRobot robot) {
        robot.clickOn(".button");
    }
     */

}
