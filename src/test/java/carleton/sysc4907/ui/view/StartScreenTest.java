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
public class StartScreenTest {

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
                false));

        //Set up and show the scene
        Scene scene = new Scene(injector.load("view/StartScreenView.fxml"), 640, 480);
        scene.getStylesheets().add("stylesheet/StartScreenStyle.css");
        stage.setScene(scene);
        stage.setTitle("Collaborative UML Diagram Tool");
        stage.show();
    }

    @Test
    public void newButtonClickedWithoutUsername(FxRobot robot) throws IOException {
        robot.clickOn("#newBtn");
        Mockito.verify(mockLoader, Mockito.never()).load(Mockito.any(Stage.class), Mockito.any(String.class), Mockito.any(String.class));
        assertTrue(robot.lookup("#newBtn").queryButton().isDisable());
    }

    @Test
    public void newButtonClickedWithUsername(FxRobot robot) throws IOException {
        Mockito.doNothing().when(mockLoader).load(Mockito.any(Stage.class), Mockito.any(String.class), Mockito.any(String.class));
        robot.clickOn("#usernameField");
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        robot.clickOn("#newBtn");
        Mockito.verify(mockLoader).load(Mockito.any(Stage.class), Mockito.any(String.class), Mockito.any(String.class));
    }

    @Test
    public void joinButtonClickedWithSpaceUsername(FxRobot robot) throws IOException {
        robot.clickOn("#usernameField");
        robot.type(KeyCode.SPACE);
        robot.clickOn("#joinBtn");
        Mockito.verify(mockLoader, Mockito.never()).load(Mockito.any(Stage.class), Mockito.any(String.class), Mockito.any(String.class));
        assertTrue(robot.lookup("#joinBtn").queryButton().isDisable());
    }

    @Test
    public void joinButtonClickedWithoutUsername(FxRobot robot) throws IOException {
        robot.clickOn("#joinBtn");
        Mockito.verify(mockLoader, Mockito.never()).load(Mockito.any(Stage.class), Mockito.any(String.class), Mockito.any(String.class));
        assertTrue(robot.lookup("#joinBtn").queryButton().isDisable());
    }

    @Test
    public void joinButtonClickedWithUsername(FxRobot robot) throws IOException, InterruptedException {
        Mockito.doNothing().when(mockLoader).load(Mockito.any(Stage.class), Mockito.any(String.class), Mockito.matches("111111111111"));
        robot.clickOn("#usernameField");
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        robot.clickOn("#joinBtn");
        robot.clickOn("#roomCodeField");
        for (int i = 0; i < 12; i++) robot.type(KeyCode.DIGIT1);
        TimeUnit.MILLISECONDS.sleep(500);
        robot.clickOn(".button");
        Mockito.verify(mockLoader).load(Mockito.any(Stage.class), Mockito.any(String.class), Mockito.matches("111111111111"));
    }

    @Test
    public void joinButtonClickedWrongRoomCode(FxRobot robot) throws IOException {
        robot.clickOn("#usernameField");
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        robot.clickOn("#joinBtn");
        robot.clickOn("#roomCodeField");
        for (int i = 0; i < 13; i++) robot.type(KeyCode.DIGIT1); // Code too long
        robot.clickOn(".button");
        Mockito.verify(mockLoader, Mockito.never()).load(Mockito.any(Stage.class), Mockito.any(String.class), Mockito.any(String.class));
    }
}
