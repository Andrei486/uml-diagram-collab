package carleton.sysc4907.ui.view;

import carleton.sysc4907.command.EditTextCommandFactory;
import carleton.sysc4907.controller.element.*;
import carleton.sysc4907.view.DiagramElement;
import carleton.sysc4907.controller.element.EditableLabelController;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class UmlEnumElementTest extends UmlBoxElementTest {




    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        super.start(stage);
    }

    @Override
    protected void addInjectionMethods() {
        dependencyInjector.addInjectionMethod(UmlEnumElementController.class,
                () -> new UmlEnumElementController(movePreviewCreator, moveCommandFactory, diagramModel,
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
            var loader = dependencyInjector.getLoader("view/element/UmlEnumElement.fxml");
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
        assertEquals("<<enumeration>>", titleText);
        assertFalse(titleLabel.getStyleClass().contains("italicised"));
    }

    @Test
    protected void correctInitialText_Entries(FxRobot robot) {
        var label = robot.lookup("#entriesLabel").queryAs(Parent.class);
        var entriesLabel = label.getChildrenUnmodifiable().stream().filter(node -> node instanceof Label).findFirst().get();
        entriesLabel.setUserData(testEntriesLabelId);

        //check the text
        String entriesText = controller.getEntriesText();
        assertEquals("Enumerators", entriesText);
        assertFalse(entriesLabel.getStyleClass().contains("italicised"));
    }


}
