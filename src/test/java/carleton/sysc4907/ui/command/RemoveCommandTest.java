package carleton.sysc4907.ui.command;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.RemoveCommand;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.DiagramElement;
import org.junit.jupiter.api.Test;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.controller.ElementLibraryPanelController;
import carleton.sysc4907.model.DiagramModel;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
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
    private DiagramElement mockDiagramElement1;

    @Mock
    private DiagramElement mockDiagramElement2;

    @Mock
    private ElementIdManager mockElementIdManager;

    @Test
    void executeRemoves() {
    /* test that these are called:
        Pane editingArea = EditingAreaProvider.getEditingArea();
        editingArea.getChildren().removeAll(args.elements());
        diagramModel.getElements().removeAll(args.elements());
        diagramModel.getSelectedElements().clear();
    */
        //setup
        long id1 = 1L;
        long id2 = 2L;
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);
            Mockito.when(mockDiagramElement1.getElementId()).thenReturn(id1);
            Mockito.when(mockDiagramElement2.getElementId()).thenReturn(id2);
            Mockito.when(mockElementIdManager.getElementById(id1)).thenReturn(mockDiagramElement1);
            Mockito.when(mockElementIdManager.getElementById(id2)).thenReturn(mockDiagramElement2);
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
            doNothing().when(mockSelectedElementsList).clear();
            List<DiagramElement> elemList = new ArrayList<>();
            elemList.add(mockDiagramElement1);
            elemList.add(mockDiagramElement2);
            long[] idList = new long[] {mockDiagramElement1.getElementId(), mockDiagramElement2.getElementId()};

            RemoveCommandArgs args = new RemoveCommandArgs(idList);
            RemoveCommand command = new RemoveCommand(args, mockDiagramModel, mockElementIdManager);

            //execute
            command.execute();

            //verify
            verify(mockNodesList).removeAll(elemList);
            verify(mockElementsList).removeAll(elemList);
            verify(mockSelectedElementsList).clear();
        }
    }
}
