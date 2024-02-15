package carleton.sysc4907.ui.view;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.DiagramEditorLoader;
import carleton.sysc4907.controller.StartScreenController;
import carleton.sysc4907.model.PreferencesModel;
import carleton.sysc4907.processing.RoomCodeManager;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class DirectConnectionDialogTest {

    @Mock
    private DiagramEditorLoader mockLoader;

    @Start
    private void start(Stage stage) throws IOException {
        //Create dependency injector to link models and controllers
        DependencyInjector injector = new DependencyInjector();
        PreferencesModel preferencesModel = new PreferencesModel();
        RoomCodeManager manager = new RoomCodeManager();

        //Add instantiation methods to the dependency injector
        injector.addInjectionMethod(StartScreenController.class, () -> new StartScreenController(
                preferencesModel,
                mockLoader,
                manager,
                true));

        //Set up and show the scene
        Scene scene = new Scene(injector.load("view/StartScreenView.fxml"), 640, 480);
        scene.getStylesheets().add("stylesheet/StartScreenStyle.css");
        stage.setScene(scene);
        stage.setTitle("Collaborative UML Diagram Tool");
        stage.show();
    }

    @Test
    public void joinButtonClicked_InvalidIpPort(FxRobot robot) throws IOException {
        robot.clickOn("#usernameField");
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        robot.clickOn("#joinBtn");
        robot.clickOn("#ipPortField");
        robot.type(KeyCode.A);
        robot.clickOn(".button");
        Mockito.verify(mockLoader, Mockito.never()).loadJoin(Mockito.any(Stage.class), Mockito.any(String.class), Mockito.any(String.class), Mockito.anyInt(), Mockito.any(Object[].class));
    }

    @Test
    public void joinButtonClickedWithUsername(FxRobot robot) throws IOException, InterruptedException {
        Mockito.doNothing().when(mockLoader).loadJoin(Mockito.any(Stage.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Object[].class));
        robot.clickOn("#usernameField");
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        robot.clickOn("#joinBtn");
        robot.clickOn("#ipPortField");

        //types 1:1, which will pass the formatting validation test
        robot.type(KeyCode.DIGIT1);
        //Pressing shift semicolon is a workaround to type ":" because KeyCode.COLON types nothing
        robot.press(KeyCode.SHIFT).press(KeyCode.SEMICOLON).release(KeyCode.SEMICOLON).release(KeyCode.SHIFT);
        robot.type(KeyCode.DIGIT1);

        TimeUnit.MILLISECONDS.sleep(500);
        robot.clickOn(".button");
        Mockito.verify(mockLoader).loadJoin(Mockito.any(Stage.class), Mockito.anyString(), Mockito.anyString(), Mockito.anyInt(), Mockito.any(Object[].class));
    }


}


