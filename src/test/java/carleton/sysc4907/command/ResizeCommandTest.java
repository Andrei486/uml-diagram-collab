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
        doNothing().when(mockNode).setMaxWidth(130.0);
        doNothing().when(mockNode).setMaxHeight(70.0);
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);
        ResizeCommandArgs args = new ResizeCommandArgs(false, true,
                10, 0, 40, -30, 0, 0, 100, 100, testId);
        ResizeCommand command = new ResizeCommand(args, mockElementIdManager);

        command.execute();

        verify(mockNode, never()).setLayoutX(Mockito.anyDouble());
        verify(mockNode, never()).setLayoutX(Mockito.anyDouble());
        verify(mockNode).setMaxWidth(130.0);
        verify(mockNode).setMaxHeight(70.0);
    }

    @Test
    void executeResizeIdempotent() {
        long testId = 12L;
        Pane mockNode = Mockito.mock(Pane.class);
        doNothing().when(mockNode).setMaxWidth(130.0);
        doNothing().when(mockNode).setMaxHeight(70.0);
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);
        ResizeCommandArgs args = new ResizeCommandArgs(false, true,
                10, 0, 40, -30, 0, 0, 100, 100, testId);
        ResizeCommand command = new ResizeCommand(args, mockElementIdManager);

        command.execute();
        command.execute();

        verify(mockNode, never()).setLayoutX(Mockito.anyDouble());
        verify(mockNode, never()).setLayoutX(Mockito.anyDouble());
        verify(mockNode, times(2)).setMaxWidth(130.0);
        verify(mockNode, times(2)).setMaxHeight(70.0);
    }

    @Test
    void executeResizeAbsolute() {
        long testId = 12L;
        Pane mockNode = Mockito.mock(Pane.class);
        doNothing().when(mockNode).setMaxWidth(130.0);
        doNothing().when(mockNode).setMaxHeight(70.0);
        doNothing().when(mockNode).setMaxWidth(160.0);
        doNothing().when(mockNode).setMaxHeight(40.0);
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);
        ResizeCommandArgs args = new ResizeCommandArgs(false, true,
                10, 0, 40, -30, 0, 0, 100, 100, testId);
        ResizeCommandArgs args2 = new ResizeCommandArgs(false, true,
                10, 0, 70, -60, 0, 0, 100, 100, testId);
        ResizeCommand command = new ResizeCommand(args, mockElementIdManager);
        ResizeCommand command2 = new ResizeCommand(args2, mockElementIdManager);

        command2.execute();

        verify(mockNode).setMaxWidth(160.0);
        verify(mockNode).setMaxHeight(40.0);

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
        doNothing().when(mockNode).setMaxWidth(130.0);
        doNothing().when(mockNode).setMaxHeight(70.0);
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockNode);
        ResizeCommandArgs args = new ResizeCommandArgs(true, false,
                10, 0, -20, 30, 0, 0, 100, 100, testId);
        ResizeCommand command = new ResizeCommand(args, mockElementIdManager);

        command.execute();

        verify(mockNode).setLayoutX(-30.0);
        verify(mockNode).setLayoutY(30.0);
        verify(mockNode).setMaxWidth(130.0);
        verify(mockNode).setMaxHeight(70.0);
    }
}
