package carleton.sysc4907.view;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.AddCommandFactory;
import carleton.sysc4907.controller.ElementLibraryPanelController;
import carleton.sysc4907.model.DiagramModel;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class ElementLibraryPanelTest {

    @Mock
    private DiagramModel mockDiagramModel;

    @Mock
    private ObservableList<DiagramElement> mockElementsList;

    @Mock
    private ObservableList<DiagramElement> mockSelectedElementsList;

    @Mock
    private ObservableList<Node> mockNodesList;

    @Mock
    private Pane editingArea;

    @Mock
    private DependencyInjector mockDependencyInjector;
    @Start
    private void start(Stage stage) throws IOException {
        Mockito.when(mockDependencyInjector.load(any(String.class))).thenReturn(new DiagramElement());
        DependencyInjector injector = new DependencyInjector();
        AddCommandFactory addCommandFactory = new AddCommandFactory(mockDiagramModel, mockDependencyInjector);
        injector.addInjectionMethod(ElementLibraryPanelController.class,
                () -> {
                    var controller = new ElementLibraryPanelController(mockDiagramModel, mockDependencyInjector, addCommandFactory);
                    controller.setEditingArea(editingArea);
                    return controller;
                });
        Scene scene = new Scene(injector.load("view/ElementLibraryPanel.fxml"));
        stage.setScene(scene);
        stage.show();
    }

    @Test
    void addRectangle(FxRobot robot) {
        Mockito.when(mockDiagramModel.getElements()).thenReturn(mockElementsList);
        Mockito.when(mockDiagramModel.getSelectedElements()).thenReturn(mockSelectedElementsList);
        Mockito.when(mockElementsList.add(any(DiagramElement.class))).thenReturn(true);
        doNothing().when(mockSelectedElementsList).clear();
        Mockito.when(editingArea.getChildren()).thenReturn(mockNodesList);
        Mockito.when(mockNodesList.add(any(Node.class))).thenReturn(true);

        robot.clickOn("#elementsPane .button");

        Mockito.verify(mockElementsList).add(any(DiagramElement.class));
        Mockito.verify(mockNodesList).add(any(Node.class));
    }
}
