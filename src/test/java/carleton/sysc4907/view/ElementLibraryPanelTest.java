package carleton.sysc4907.view;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.AddCommandFactory;
import carleton.sysc4907.controller.ElementLibraryPanelController;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;
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
    private ObservableList<Node> mockNodesList;

    @Mock
    private Pane editingArea;

    @Mock
    private ElementCreator mockElementCreator;

    @Mock
    private DiagramElement mockDiagramElement;

    @Start
    private void start(Stage stage) throws IOException {
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(editingArea);
            Mockito.when(editingArea.getChildren()).thenReturn(mockNodesList);
            Collection<String> types = new HashSet<>();
            types.add("rectangleType");
            Mockito.when(mockElementCreator.getRegisteredTypes()).thenReturn(types);
            DependencyInjector injector = new DependencyInjector();
            AddCommandFactory addCommandFactory = new AddCommandFactory(mockDiagramModel, mockElementCreator);
            injector.addInjectionMethod(ElementLibraryPanelController.class,
                    () -> {
                        var controller = new ElementLibraryPanelController(
                                mockDiagramModel,
                                addCommandFactory,
                                mockElementCreator);
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
        EditingAreaProvider.init(editingArea);
        Mockito.when(mockDiagramModel.getElements()).thenReturn(mockElementsList);
        Mockito.when(mockElementsList.add(any(DiagramElement.class))).thenReturn(true);
        Mockito.when(editingArea.getChildren()).thenReturn(mockNodesList);
        Mockito.when(mockNodesList.add(any(Node.class))).thenReturn(true);
        Mockito.when(mockElementCreator.create("rectangleType"))
                .thenReturn(mockDiagramElement);

        robot.clickOn("#elementsPane .button");

        Mockito.verify(mockElementsList).add(any(DiagramElement.class));
        Mockito.verify(mockNodesList).add(any(Node.class));
        EditingAreaProvider.init(null);
    }
}
