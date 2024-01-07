package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ResizeCommandArgs;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResizeCommandTest {

    @Mock
    private Node mockNode;
    @Mock
    private ElementIdManager mockElementIdManager;

    @Test
    void executeResizeBottomRight() {
        long testId = 12L;
        Pane mockNode = Mockito.mock(Pane.class);
        Mockito.when(mockNode.getMaxHeight()).thenReturn(100.0);
        Mockito.when(mockNode.getMaxWidth()).thenReturn(100.0);
        doNothing().when(mockNode).setMaxWidth(130.0);
        doNothing().when(mockNode).setMaxHeight(70.0);
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);
        ResizeCommandArgs args = new ResizeCommandArgs(false, true,
                10, 0, 40, -30, testId);
        ResizeCommand command = new ResizeCommand(args, mockElementIdManager);

        command.execute();

        verify(mockNode, never()).setLayoutX(Mockito.anyDouble());
        verify(mockNode, never()).setLayoutX(Mockito.anyDouble());
        verify(mockNode).setMaxWidth(130.0);
        verify(mockNode).setMaxHeight(70.0);
    }

    @Test
    void executeResizeTopLeft() {
        long testId = 12L;
        Pane mockNode = Mockito.mock(Pane.class);
        Mockito.when(mockNode.getLayoutX()).thenReturn(0.0);
        Mockito.when(mockNode.getLayoutY()).thenReturn(0.0);
        Mockito.when(mockNode.getMaxHeight()).thenReturn(100.0);
        Mockito.when(mockNode.getMaxWidth()).thenReturn(100.0);
        doNothing().when(mockNode).setMaxWidth(130.0);
        doNothing().when(mockNode).setMaxHeight(70.0);
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);
        ResizeCommandArgs args = new ResizeCommandArgs(true, false,
                10, 0, -20, 30, testId);
        ResizeCommand command = new ResizeCommand(args, mockElementIdManager);

        command.execute();

        verify(mockNode).setLayoutX(-30.0);
        verify(mockNode).setLayoutY(30.0);
        verify(mockNode).setMaxWidth(130.0);
        verify(mockNode).setMaxHeight(70.0);
    }
}
