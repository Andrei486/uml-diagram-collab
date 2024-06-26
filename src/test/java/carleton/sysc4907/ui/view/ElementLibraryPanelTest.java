package carleton.sysc4907.ui.view;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.AddCommandFactory;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.controller.ElementLibraryPanelController;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.EditingAreaCoordinateProvider;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class ElementLibraryPanelTest {

    @Mock
    private DiagramModel mockDiagramModel;

    @Mock
    private ObservableList<DiagramElement> mockElementsList;

    @Mock
    private ObservableList<Node> mockNodesList;

    @Mock
    private Pane editingArea;

    @Mock
    private ElementCreator mockElementCreator;

    @Mock
    private ExecutedCommandList mockExecutedCommandList;

    @Mock
    private DiagramElement mockDiagramElement;

    @Mock
    private ElementIdManager mockElementIdManager;

    @Mock
    private MessageConstructor mockMessageConstructor;

    @Mock
    private EditingAreaCoordinateProvider coordinateProvider;

    @Start
    private void start(Stage stage) throws IOException {
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(editingArea);
            Mockito.when(editingArea.getChildren()).thenReturn(mockNodesList);
            Collection<String> types = new HashSet<>();
            types.add("rectangleType");
            Mockito.when(mockElementCreator.getRegisteredTypes()).thenReturn(types);
            DependencyInjector injector = new DependencyInjector();

            Manager mockManager = Mockito.mock(Manager.class);
            // Make sure commands are run as host, running them as client does nothing
            lenient().when(mockManager.isHost()).thenReturn(true);
            AddCommandFactory addCommandFactory = new AddCommandFactory(mockDiagramModel, mockElementCreator, mockManager, mockExecutedCommandList, mockMessageConstructor);
            injector.addInjectionMethod(ElementLibraryPanelController.class,
                    () -> {
                        var controller = new ElementLibraryPanelController(
                                mockDiagramModel,
                                addCommandFactory,
                                mockElementCreator,
                                mockElementIdManager,
                                coordinateProvider);
                        return controller;
                    });
            Scene scene = new Scene(injector.load("view/ElementLibraryPanel.fxml"));
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void addRectangle(FxRobot robot) {
        long testId = 12L;
        EditingAreaProvider.init(editingArea);
        Mockito.when(mockDiagramModel.getElements()).thenReturn(mockElementsList);
        Mockito.when(mockElementsList.add(any(DiagramElement.class))).thenReturn(true);
        Mockito.when(editingArea.getChildren()).thenReturn(mockNodesList);
        Mockito.when(mockNodesList.add(any(Node.class))).thenReturn(true);
        Mockito.when(mockElementCreator.create("rectangleType", testId, true))
                .thenReturn(mockDiagramElement);
        Mockito.when(mockExecutedCommandList.getCommandList()).thenReturn(new LinkedList<>());
        Mockito.when(mockElementIdManager.getNewIdRange(anyInt())).thenReturn(testId);
        Mockito.when(coordinateProvider.getCenterVisibleX()).thenReturn(100.0);
        Mockito.when(coordinateProvider.getCenterVisibleY()).thenReturn(400.0);

        robot.clickOn("#elementsPane .button");

        Mockito.verify(mockElementsList).add(any(DiagramElement.class));
        Mockito.verify(mockNodesList).add(any(Node.class));
        Mockito.verify(mockDiagramElement).setLayoutX(100.0);
        Mockito.verify(mockDiagramElement).setLayoutY(400.0);
        EditingAreaProvider.init(null);
    }
}
