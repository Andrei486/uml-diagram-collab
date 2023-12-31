package carleton.sysc4907.command;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.view.DiagramElement;
import javafx.collections.transformation.SortedList;
import org.junit.jupiter.api.Test;

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
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.MockedStatic.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class RemoveCommandTest {

    @Mock
    private DiagramModel mockDiagramModel;

    @Mock
    private ObservableList<DiagramElement> mockElementsList;

    @Mock
    private ObservableList<DiagramElement> mockSelectedElementsList;

    @Mock
    private ObservableList<Node> mockNodesList;

    @Mock
    private Pane mockEditingArea;

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
                    controller.setEditingArea(mockEditingArea);
                    return controller;
                });

    }

    @Test
    void executeRemoves() {
    /* test that these are called:
        Pane editingArea = EditingAreaProvider.getEditingArea();
        editingArea.getChildren().removeAll(args.elements());
        diagramModel.getElements().removeAll(args.elements());
        diagramModel.getSelectedElements().clear();
    */
        //setup
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);
            Mockito.when(mockDiagramModel.getElements())
                    .thenReturn(mockElementsList);
            Mockito.when(mockDiagramModel.getSelectedElements())
                    .thenReturn(mockSelectedElementsList);
            Mockito.when(mockEditingArea.getChildren())
                    .thenReturn(mockNodesList);
            Mockito.when(mockNodesList.removeAll(any(List.class))).
                    thenReturn(true);
            Mockito.when(mockElementsList.removeAll(any(List.class)))
                    .thenReturn(true);
            doThrow(UnsupportedOperationException.class)
                    .when(mockSelectedElementsList)
                    .clear();
/*
        doThrow(UnsupportedOperationException.class)
                .when(mockSelectedElementsList)
                .clear();

        doAnswer(mockSelectedElementsList.clear() -> {
            return null;
        }).when(mockSelectedElementsList.clear()).thenReturn(null);

         */
            doNothing().when(mockSelectedElementsList).clear();
            DiagramElement diagramElement1 = new DiagramElement();
            DiagramElement diagramElement2 = new DiagramElement();
            List<DiagramElement> elemList = new ArrayList<>();
            elemList.add(diagramElement1);
            elemList.add(diagramElement2);
           // ObservableList<DiagramElement> argList = new SortedList<DiagramElement>(elemList);

            RemoveCommandArgs args = new RemoveCommandArgs(elemList);
            RemoveCommand command = new RemoveCommand(args, mockDiagramModel);

            //execute
            command.execute();

            //verify
            verify(mockNodesList).removeAll(any(List.class));
            verify(mockEditingArea).getChildren().removeAll(elemList);
            verify(mockElementsList).removeAll();
            verify(mockSelectedElementsList).clear();
        }

    }
    /*
    @Test
void givenDoAnswer_whenAddCalled_thenAnswered() {
    MyList myList = mock(MyList.class);

    doAnswer(invocation -> {
        Object arg0 = invocation.getArgument(0);
        Object arg1 = invocation.getArgument(1);

        assertEquals(3, arg0);
        assertEquals("answer me", arg1);
        return null;
    }).when(myList).add(any(Integer.class), any(String.class));

    myList.add(3, "answer me");
}


    @Test
void givenStaticMethodWithArgs_whenMocked_thenReturnsMockSuccessfully() {
    assertThat(StaticUtils.range(2, 6)).containsExactly(2, 3, 4, 5);

    try (MockedStatic<StaticUtils> utilities = Mockito.mockStatic(StaticUtils.class)) {
        utilities.when(() -> StaticUtils.range(2, 6))
          .thenReturn(Arrays.asList(10, 11, 12));

        assertThat(StaticUtils.range(2, 6)).containsExactly(10, 11, 12);
    }

    assertThat(StaticUtils.range(2, 6)).containsExactly(2, 3, 4, 5);
}


     */
}
