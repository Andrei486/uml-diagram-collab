package carleton.sysc4907.view;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.controller.DiagramEditingAreaController;
import carleton.sysc4907.model.DiagramElement;
import carleton.sysc4907.model.DiagramModel;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class DiagramEditingAreaTest {

    @Mock
    private DiagramModel mockDiagramModel;

    @Mock
    private ObservableList<DiagramElement> mockSelectedElementsList;

    private Pane editingArea;

    private ScrollPane scrollPane;

    @Start
    private void start(Stage stage) throws IOException {
        DependencyInjector injector = new DependencyInjector();
        injector.addInjectionMethod(DiagramEditingAreaController.class,
                () -> new DiagramEditingAreaController(mockDiagramModel));
        Scene scene = new Scene(injector.load("view/DiagramEditingArea.fxml"));
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void clickOnPaneDeselectsAll(FxRobot robot) {
        editingArea = robot.lookup("#editingArea").queryAs(Pane.class);
        scrollPane = robot.lookup("#scrollPane").queryAs(ScrollPane.class);
        Mockito.when(mockDiagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        doNothing().when(mockSelectedElementsList).clear();

        robot.clickOn(editingArea);

        verify(mockSelectedElementsList).clear();
    }
}
