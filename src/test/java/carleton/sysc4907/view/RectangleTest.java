package carleton.sysc4907.view;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.controller.element.RectangleController;
import carleton.sysc4907.controller.element.ResizeHandleCreator;
import carleton.sysc4907.model.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;

public class RectangleTest extends DiagramElementTest {

    private ResizeHandleCreator resizeHandleCreator;

    @Start
    @Override
    protected void start(Stage stage) throws IOException {
        super.start(stage);
        resizeHandleCreator = new ResizeHandleCreator();
    }

    @Override
    protected void addInjectionMethods() {
        dependencyInjector.addInjectionMethod(RectangleController.class,
                () -> new RectangleController(movePreviewCreator, moveCommandFactory, diagramModel, resizeHandleCreator));
    }

    @Override
    protected DiagramElement loadElement() {
        try {
            return (DiagramElement) dependencyInjector.load("view/element/Rectangle.fxml");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
