package carleton.sysc4907.ui.integration;

import carleton.sysc4907.App;
import carleton.sysc4907.view.DiagramElement;
import javafx.application.Application;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.service.query.PointQuery;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class DiagramIntegrationTest {

    protected final String SELECTED_STYLE_CLASS = "selected-element";

    private Application app;

    @BeforeEach
    public void launchApp() throws Exception {
        this.app = launch(App.class);
    }

    private Application launch(Class<? extends Application> appClass, String... appArgs) throws Exception {
        FxToolkit.registerPrimaryStage();
        return FxToolkit.setupApplication(appClass, appArgs);
    }

    private Set<Node> lookupAllDiagramElements(FxRobot robot) {
        Pane editingArea = robot.lookup("#editingArea").query();
        return editingArea.lookupAll("#element");
    }

    @Test
    public void launchApplication(FxRobot robot) throws InterruptedException {
//        TimeUnit.MILLISECONDS.sleep(500);
        robot.clickOn("#usernameField");
        robot.type(KeyCode.T, KeyCode.E, KeyCode.S, KeyCode.T);
        robot.clickOn("#newBtn");
//        TimeUnit.MILLISECONDS.sleep(500);
        Pane editingArea = robot.lookup("#editingArea").query();
        robot.clickOn(editingArea);
        Parent elementLibraryPane = robot.lookup("#elementsPane").query();
        assertNotNull(elementLibraryPane);
    }

    @Test
    public void addAndDeleteElements(FxRobot robot) throws InterruptedException {
        launchApplication(robot);
        Parent elementLibraryPane = robot.lookup("#elementsPane").query();
        // create one of each element
        for (var button : elementLibraryPane.lookupAll(".button")) {
            robot.clickOn(button);
            TimeUnit.MILLISECONDS.sleep(100);
            var elements = lookupAllDiagramElements(robot);
            assertEquals(1, elements.size());
            Node node = elements.stream().findFirst().get();
            System.out.println(node);
            System.out.println(node.getUserData());
            robot.clickOn(node);
            System.out.println(node.getStyleClass());
            assertTrue(node.getStyleClass().contains(SELECTED_STYLE_CLASS));
            TimeUnit.MILLISECONDS.sleep(100);
            robot.press(KeyCode.DELETE);
            robot.release(KeyCode.DELETE);
        }
    }

    @Test
    public void exitDiagramEditor(FxRobot robot) throws InterruptedException {
        launchApplication(robot);
        robot.press(KeyCode.ALT, KeyCode.F, KeyCode.X);
        var elementLibraryPane = robot.lookup("#elementsPane").tryQuery();
        assertFalse(elementLibraryPane.isPresent());
    }

    @Test
    public void editTextStyling(FxRobot robot) throws InterruptedException {
        launchApplication(robot);
        Parent elementLibraryPane = robot.lookup("#elementsPane").query();
        var addButton = elementLibraryPane.lookupAll(".button").stream().filter(
                b -> Objects.equals(((Button) b).getText(), "UML Class")).findFirst().orElse(null);
        assertNotNull(addButton);
        robot.clickOn(addButton);
        TimeUnit.MILLISECONDS.sleep(100);
        var element = robot.lookup("#element").queryAs(DiagramElement.class);
        var titleLabel = element.lookup("#titleLabel");
        robot.doubleClickOn(titleLabel);
        var boldButton = robot.lookup("#boldButton").queryAs(ToggleButton.class);
        robot.clickOn(boldButton);
        var italicsButton = robot.lookup("#italicsButton").queryAs(ToggleButton.class);
        robot.clickOn(italicsButton);
        var underlineButton = robot.lookup("#underlineButton").queryAs(ToggleButton.class);
        robot.clickOn(underlineButton);
        var fontSizeSpinner = robot.lookup("#fontSize").queryAs(Spinner.class);
        robot.clickOn(fontSizeSpinner);
        robot.press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE).press(KeyCode.BACK_SPACE).release(KeyCode.BACK_SPACE);
        robot.press(KeyCode.DIGIT1, KeyCode.DIGIT7, KeyCode.ENTER);
        var titleLabelVisibleLabel = titleLabel.lookup("#label");

        assertTrue(boldButton.isSelected());
        assertTrue(italicsButton.isSelected());
        assertTrue(underlineButton.isSelected());
        assertTrue(titleLabelVisibleLabel.getStyleClass().contains("bolded"));
        assertTrue(titleLabelVisibleLabel.getStyleClass().contains("italicized"));
        assertTrue(titleLabelVisibleLabel.getStyleClass().contains("underlined"));
        assertEquals(17.0, fontSizeSpinner.getValue());

        var fieldsLabel = element.lookup("#fieldsLabel");
        robot.doubleClickOn(fieldsLabel);

        assertFalse(boldButton.isSelected());
        assertFalse(italicsButton.isSelected());
        assertFalse(underlineButton.isSelected());
        assertTrue(titleLabelVisibleLabel.getStyleClass().contains("bolded"));
        assertTrue(titleLabelVisibleLabel.getStyleClass().contains("italicized"));
        assertTrue(titleLabelVisibleLabel.getStyleClass().contains("underlined"));
        assertNotEquals(17, fontSizeSpinner.getValue());

        robot.doubleClickOn(titleLabel);
        assertTrue(boldButton.isSelected());
        assertTrue(italicsButton.isSelected());
        assertTrue(underlineButton.isSelected());
        assertEquals(17.0, fontSizeSpinner.getValue());
    }
}
