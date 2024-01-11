package carleton.sysc4907.ui.command;
import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.command.AddCommand;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.processing.ElementCreator;
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
public class AddCommandTest {
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
    private ElementCreator mockElementCreator;

    @Mock
    private DiagramElement mockDiagramElement;

    @Test
    void executeAdds() {
        /* test that these are called:
        editingArea.getChildren().add(obj);
        diagramModel.getElements().add(element);
         */
        //setup
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);
            Mockito.when(mockDiagramModel.getElements())
                    .thenReturn(mockElementsList);
            Mockito.when(mockEditingArea.getChildren())
                    .thenReturn(mockNodesList);
            Mockito.when(mockEditingArea.getChildren())
                    .thenReturn(mockNodesList);
            Mockito.when(mockNodesList.add(any(Node.class))).
                    thenReturn(true);
            Mockito.when(mockElementsList.add(any(DiagramElement.class)))
                    .thenReturn(true);
            Mockito.when(mockElementCreator.create(eq("testType"), anyLong()))
                    .thenReturn(mockDiagramElement);

            AddCommandArgs args = new AddCommandArgs("testType", 2L);
            AddCommand command = new AddCommand(args, mockDiagramModel, mockElementCreator);

            //execute
            command.execute();

            //verify
            verify(mockNodesList).add(any(Node.class));
            verify(mockElementsList).add(any(DiagramElement.class));
        }

    }



}
