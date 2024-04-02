package carleton.sysc4907.ui.view;

import carleton.sysc4907.command.EditTextCommandFactory;
import carleton.sysc4907.controller.element.EditableLabelController;
import carleton.sysc4907.controller.element.UmlEnumElementController;
import carleton.sysc4907.controller.element.UmlInterfaceElementController;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UmlInterfaceElementTest extends UmlBoxElementTest {

    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        super.start(stage);
    }

    @Override
    protected void addInjectionMethods() {
        dependencyInjector.addInjectionMethod(UmlInterfaceElementController.class,
                () -> new UmlInterfaceElementController(movePreviewCreator, moveCommandFactory, diagramModel,
                        resizeHandleCreator, resizePreviewCreator, resizeCommandFactory));
        dependencyInjector.addInjectionMethod(EditableLabelController.class,
                () -> new EditableLabelController(new EditTextCommandFactory(
                        elementIdManager,
                        mockManager,
                        mockExecutedCommandList,
                        mockMessageConstructor),
                        editableLabelTracker
                ));
    }

    @Override
    protected DiagramElement loadElement() {
        try {
            var loader = dependencyInjector.getLoader("view/element/UmlInterfaceElement.fxml");
            var newElement = (DiagramElement) loader.load();
            controller = loader.getController();
            return newElement;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    protected void correctInitialText_Title(FxRobot robot) {
        var label = robot.lookup("#titleLabel").queryAs(Parent.class);
        var titleLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        titleLabel.setUserData(testTitleLabelId);

        //check the text
        String titleText = controller.getTitleText();
        assertEquals("<<interface>>", titleText);
        assertTrue(titleLabel.getStyleClass().contains("italicized"));
    }

    @Test
    protected void correctInitialText_Entries(FxRobot robot) {
        var label = robot.lookup("#entriesLabel").queryAs(Parent.class);
        var entriesLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        entriesLabel.setUserData(testEntriesLabelId);

        //check the text
        String entriesText = controller.getEntriesText();
        assertEquals("Methods", entriesText);
        assertFalse(entriesLabel.getStyleClass().contains("italicised"));
    }


}
