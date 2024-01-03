package carleton.sysc4907.command;

import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.command.args.ResizeCommandArgs;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;

public class ResizeCommandTest {

    @Test
    void executeResizeBottomRight() {
        Pane mockNode = Mockito.mock(Pane.class);
        Mockito.when(mockNode.getMaxHeight()).thenReturn(100.0);
        Mockito.when(mockNode.getMaxWidth()).thenReturn(100.0);
        doNothing().when(mockNode).setMaxWidth(130.0);
        doNothing().when(mockNode).setMaxHeight(70.0);
        ResizeCommandArgs args = new ResizeCommandArgs(false, true,
                10, 0, 40, -30, mockNode);
        ResizeCommand command = new ResizeCommand(args);

        command.execute();

        verify(mockNode, never()).setLayoutX(Mockito.anyDouble());
        verify(mockNode, never()).setLayoutX(Mockito.anyDouble());
        verify(mockNode).setMaxWidth(130.0);
        verify(mockNode).setMaxHeight(70.0);
    }

    @Test
    void executeResizeTopLeft() {
        Pane mockNode = Mockito.mock(Pane.class);
        Mockito.when(mockNode.getLayoutX()).thenReturn(0.0);
        Mockito.when(mockNode.getLayoutY()).thenReturn(0.0);
        Mockito.when(mockNode.getMaxHeight()).thenReturn(100.0);
        Mockito.when(mockNode.getMaxWidth()).thenReturn(100.0);
        doNothing().when(mockNode).setMaxWidth(130.0);
        doNothing().when(mockNode).setMaxHeight(70.0);
        ResizeCommandArgs args = new ResizeCommandArgs(true, false,
                10, 0, -20, 30, mockNode);
        ResizeCommand command = new ResizeCommand(args);

        command.execute();

        verify(mockNode).setLayoutX(-30.0);
        verify(mockNode).setLayoutY(30.0);
        verify(mockNode).setMaxWidth(130.0);
        verify(mockNode).setMaxHeight(70.0);
    }
}
