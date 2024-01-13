package carleton.sysc4907.ui.view;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.controller.FormattingPanelController;
import carleton.sysc4907.model.TextFormattingModel;
import javafx.collections.FXCollections;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ToggleButton;
import javafx.stage.Stage;
import org.testfx.assertions.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(ApplicationExtension.class)
public class FormattingPanelTest {

    private TextFormattingModel mockTextFormattingModel;

    @Start
    private void start(Stage stage) throws IOException {
        mockTextFormattingModel = Mockito.mock(TextFormattingModel.class);
        List<String> fonts = new LinkedList<>();
        fonts.add("Test Font 1");
        fonts.add("Test Font 2");
        Mockito.when(mockTextFormattingModel.getFontFamilyNames()).thenReturn(FXCollections.observableList(fonts));
        DependencyInjector injector = new DependencyInjector();
        injector.addInjectionMethod(FormattingPanelController.class,
                () -> new FormattingPanelController(mockTextFormattingModel));
        Scene scene = new Scene(injector.load("view/FormattingPanel.fxml"));
        stage.setScene(scene);
        stage.show();
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

        for (ToggleButton button : buttons) {
            assertFalse(button.disabledProperty().get());
            robot.clickOn(button);
            assertTrue(button.selectedProperty().get());
            robot.clickOn(button);
            assertFalse(button.selectedProperty().get());
        }
    }
}
