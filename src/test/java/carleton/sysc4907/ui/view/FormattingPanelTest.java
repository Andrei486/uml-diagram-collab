package carleton.sysc4907.ui.view;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.ChangeTextStyleCommand;
import carleton.sysc4907.command.ChangeTextStyleCommandFactory;
import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.controller.FormattingPanelController;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.EditableLabelTracker;
import carleton.sysc4907.model.TextFormattingModel;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class FormattingPanelTest {

    @Mock
    private ChangeTextStyleCommandFactory changeTextStyleCommandFactory;

    @Mock
    private ChangeTextStyleCommand mockChangeTextStyleCommand;

    @Mock
    private ElementIdManager elementIdManager;

    @Mock
    private DiagramModel mockDiagramModel;

    private EditableLabelTracker editableLabelTracker;

    @Mock
    private TextFormattingModel mockTextFormattingModel;

    private ObservableList<DiagramElement> selectedElemsList;

    @Start
    private void start(Stage stage) throws IOException {
        //instead of instantiating an element, make the editableLabelTracker have values stored
        mockTextFormattingModel = Mockito.mock(TextFormattingModel.class);
        String fontFamily = "Segoe UI";
        double fontSize = 14d;
        editableLabelTracker = new EditableLabelTracker(elementIdManager);
        editableLabelTracker.getIsBoldedProperty().set(false);
        editableLabelTracker.getIsUnderlinedProperty().set(false);
        editableLabelTracker.getIsItalicizedProperty().set(false);
        editableLabelTracker.getFontSizeProperty().set(fontSize);
        editableLabelTracker.getFontFamilyProperty().set(fontFamily);

        Label label = new Label();
        label.setStyle("-fx-font-family: \"" + fontFamily + "\"; -fx-font-size: " + fontSize + ";");
        selectedElemsList = FXCollections.observableList(new ArrayList<>());
        selectedElemsList.add(new DiagramElement());

        Mockito.when(elementIdManager.getElementById(anyLong())).thenReturn(label);
        Mockito.when(mockDiagramModel.getSelectedElements()).thenReturn(selectedElemsList);
        Mockito.lenient().when(changeTextStyleCommandFactory.createTracked(any(ChangeTextStyleCommandArgs.class))).thenReturn(mockChangeTextStyleCommand);
        Mockito.lenient().doNothing().when(mockChangeTextStyleCommand).execute();
        List<String> fonts = new LinkedList<>();
        fonts.add("Test Font 1");
        fonts.add("Test Font 2");
        Mockito.when(mockTextFormattingModel.getFontFamilyNames()).thenReturn(FXCollections.observableList(fonts));
        DependencyInjector injector = new DependencyInjector();
        injector.addInjectionMethod(FormattingPanelController.class,
                () -> new FormattingPanelController(mockTextFormattingModel, changeTextStyleCommandFactory, mockDiagramModel, editableLabelTracker, elementIdManager));
        var root = injector.load("view/FormattingPanel.fxml");
        Scene deadScene = new Scene(label);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

        //this will trigger the formatting panel to update
        editableLabelTracker.setIdLastEditedLabel(1234L);
    }

    @Test
    public void allFontNamesInList(FxRobot robot) {
        ComboBox fontNames = robot.lookup("#fontName").queryAs(ComboBox.class);
        assertTrue(fontNames.getItems().contains("Test Font 1"));
        assertTrue(fontNames.getItems().contains("Test Font 2"));
        assertEquals(2, fontNames.getItems().size());

        Mockito.verify(mockTextFormattingModel).getFontFamilyNames();
    }

    @Test
    public void formattingButtonsToggle(FxRobot robot) {
        ToggleButton boldButton = robot.lookup("#boldButton").queryAs(ToggleButton.class);
        ToggleButton italicsButton = robot.lookup("#italicsButton").queryAs(ToggleButton.class);
        ToggleButton underlineButton = robot.lookup("#underlineButton").queryAs(ToggleButton.class);

        List<ToggleButton> buttons = new LinkedList<>();
        buttons.add(boldButton);
        buttons.add(italicsButton);
        buttons.add(underlineButton);

        //select the label
        editableLabelTracker.setIdLastEditedLabel(1234L);

        for (ToggleButton button : buttons) {
            assertFalse(button.disabledProperty().get());
            robot.clickOn(button);
            assertTrue(button.selectedProperty().get());
            robot.clickOn(button);
            assertFalse(button.selectedProperty().get());
        }

        //check that a command is fired (2 for each button type)
        Mockito.verify(mockChangeTextStyleCommand, Mockito.times(6)).execute();

    }

    @Test
    public void fontSizeChangeSendsCommand(FxRobot robot) throws InterruptedException {
        Spinner spinner = robot.lookup("#fontSize").queryAs(Spinner.class);
        Label label = (Label) elementIdManager.getElementById(1234L);
        label.applyCss();
        double defaultSize = 14;
        double newSize = 7;

        //select the label
        Platform.runLater(() -> editableLabelTracker.setIdLastEditedLabel(1234L));
        TimeUnit.MILLISECONDS.sleep(1000);

        assertEquals(defaultSize, label.getFont().getSize());
        assertEquals(defaultSize, spinner.getValue());

        //make the spinner have an empty value
        robot.clickOn(spinner);
        robot.type(KeyCode.BACK_SPACE);
        robot.type(KeyCode.BACK_SPACE);
        robot.type(KeyCode.ENTER);

        //put a new value in the spinner
        robot.clickOn(spinner);
        robot.type(KeyCode.DIGIT7);
        robot.type(KeyCode.ENTER);
        assertEquals(newSize, spinner.getValue());

        //this should only fire 1 command, because if the value is null it will not
        Mockito.verify(mockChangeTextStyleCommand, Mockito.times(1)).execute();
    }

    /**
     * Tests that the UI elements reflect changes in the editableLabelTracker.
     * Make sure it doesn't fire commands when this happens
     */

    /**
     * Tests the handler for last edited label ID changed
     */

    /**
     * Tests that the formatting panel is enabled and disabled when it should be
     */

    /**
     * Try entering out of bounds values in the spinner
     */
}
