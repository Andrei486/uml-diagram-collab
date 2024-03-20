package carleton.sysc4907.command;

import carleton.sysc4907.command.args.*;
import carleton.sysc4907.controller.element.ConnectorElementController;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;
import carleton.sysc4907.view.SnapHandle;
import javafx.collections.ObservableMap;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommandListCompressorTest {

    @Mock
    private DiagramModel diagramModel;

    @Mock
    private ElementIdManager elementIdManager;

    @Mock
    private ElementCreator elementCreator;

    @Mock
    private ConnectorElementController connectorElementController;

    @Mock
    private SnapHandle snapHandle;

    @Mock
    private SnapHandle snapHandle2;

    /**
     * Test that for each command type that has no additional special logic, only the last command of that
     * type is returned in the compressed list.
     */
    @Test
    void testStandardCommandTypesSavedOnlyLast() {
        var compressor = new CommandListCompressor(diagramModel, elementIdManager);
        var testId = 12L;
        var testId2 = 13L;
        List<Command<?>> commandList = new LinkedList<>();
        commandList.add(new AddCommand(new AddCommandArgs("test", testId), diagramModel, elementCreator));
        var addCmd = new AddCommand(new AddCommandArgs("test", testId), diagramModel, elementCreator);
        commandList.add(addCmd);
        commandList.add(new EditTextCommand(new EditTextCommandArgs("new text", testId2), elementIdManager));
        var editCmd = new EditTextCommand(new EditTextCommandArgs("newer text", testId2), elementIdManager);
        commandList.add(editCmd);
        commandList.add(new ResizeCommand(new ResizeCommandArgs(
                false, true,
                0, 0,
                0, 2,
                3, 0,
                0, 3,
                testId), elementIdManager));
        var resizeCmd = new ResizeCommand(new ResizeCommandArgs(
                false, true,
                1, 0,
                0, 2,
                0, 0,
                4, 0,
                testId), elementIdManager);
        commandList.add(resizeCmd);
        commandList.add(new MoveCommand(new MoveCommandArgs(0, 0, 0, 1, testId2), elementIdManager));
        var moveCmd = new MoveCommand(new MoveCommandArgs(0, 0, 1, 1, testId2), elementIdManager);
        commandList.add(moveCmd);
        when(elementIdManager.getUsedIds()).thenReturn(new LinkedList<>(Arrays.asList(testId, testId2)));

        var compressedList = compressor.compressCommandList(commandList);
        assertEquals(4, compressedList.size());
        assertTrue(compressedList.contains(addCmd));
        assertTrue(compressedList.contains(editCmd));
        assertTrue(compressedList.contains(resizeCmd));
        assertTrue(compressedList.contains(moveCmd));
    }

    @Test
    void testNoCommandsSavedForNonExistentElements() {
        var compressor = new CommandListCompressor(diagramModel, elementIdManager);
        var testId = 12L;
        var testId2 = 13L;
        List<Command<?>> commandList = new LinkedList<>();
        commandList.add(new AddCommand(new AddCommandArgs("test", testId), diagramModel, elementCreator));
        commandList.add(new EditTextCommand(new EditTextCommandArgs("new text", testId2), elementIdManager));
        commandList.add(new ResizeCommand(new ResizeCommandArgs(
                false, true,
                0, 0,
                0, 2,
                3, 0,
                0, 3,
                testId), elementIdManager));
        commandList.add(new MoveCommand(new MoveCommandArgs(0, 0, 0, 1, testId2), elementIdManager));
        when(elementIdManager.getUsedIds()).thenReturn(new LinkedList<>());

        var compressedList = compressor.compressCommandList(commandList);
        assertEquals(0, compressedList.size());
    }

    /**
     * Tests that the last move command is saved for a given element, including resizes.
     * More than one move command saved is fine.
     */
    @Test
    void testMoveCommandsLastSaved() {
        var compressor = new CommandListCompressor(diagramModel, elementIdManager);
        var testId = 12L;
        when(elementIdManager.getUsedIds()).thenReturn(new LinkedList<>(Arrays.asList(testId)));
        var moveCmd = new MoveCommand(new MoveCommandArgs(0, 0, 1, 1, testId), elementIdManager);
        var resizeCmd = new ResizeCommand(new ResizeCommandArgs(
                true, true, // not bottom right, counts as a move
                1, 0,
                0, 2,
                0, 0,
                4, 0,
                testId), elementIdManager);
        var resizeCmd2 = new ResizeCommand(new ResizeCommandArgs(
                false, true, // bottom right, does not count as a move
                1, 0,
                0, 2,
                0, 0,
                4, 0,
                testId), elementIdManager);
        List<Command<?>> commandList = new LinkedList<>();
        commandList.add(moveCmd); // The last regular move may be saved as per standard logic
        commandList.add(resizeCmd); // This resize must be saved because it is a move
        commandList.add(resizeCmd2); // The last resize will be saved as per standard logic

        var compressedList = compressor.compressCommandList(commandList);

        assertTrue(compressedList.contains(resizeCmd));
        assertTrue(compressedList.contains(resizeCmd2));
    }

    /**
     * Tests that the last connector move points and snaps are saved for EACH endpoint of the connector.
     * This is different from standard logic, where only 1 command per element would be saved, here there could be 2.
     */
    @Test
    void testConnectorMovePointsAndSnapsBothSavedOnce() {
        var compressor = new CommandListCompressor(diagramModel, elementIdManager);
        var testId = 12L;
        when(elementIdManager.getUsedIds()).thenReturn(new LinkedList<>(Arrays.asList(testId)));
        var startMoveArgs = new ConnectorMovePointCommandArgs(
                true, 0, 1, 2, 3, testId
        );
        var startConnectorMoveCommand = new ConnectorMovePointCommand(startMoveArgs, elementIdManager);
        var endMoveArgs = new ConnectorMovePointCommandArgs(
                false, 0, 1, 2, 3, testId
        );
        var endConnectorMoveCommand = new ConnectorMovePointCommand(endMoveArgs, elementIdManager);
        var startSnapArgs = new ConnectorSnapCommandArgs(
                testId, true, false, 100L
        );
        var startConnectorSnapCommand = new ConnectorSnapCommand(startSnapArgs, elementIdManager);
        var endSnapArgs = new ConnectorSnapCommandArgs(
                testId, false, false, 100L
        );
        var endConnectorSnapCommand = new ConnectorSnapCommand(endSnapArgs, elementIdManager);

        List<Command<?>> commandList = new LinkedList<>();
        commandList.add(new ConnectorMovePointCommand(startMoveArgs, elementIdManager));
        commandList.add(new ConnectorMovePointCommand(endMoveArgs, elementIdManager));
        commandList.add(new ConnectorSnapCommand(startSnapArgs, elementIdManager));
        commandList.add(new ConnectorSnapCommand(endSnapArgs, elementIdManager));
        commandList.add(startConnectorMoveCommand);
        commandList.add(endConnectorSnapCommand);
        commandList.add(endConnectorMoveCommand);
        commandList.add(startConnectorSnapCommand);

        var compressedList = compressor.compressCommandList(commandList);

        assertEquals(4, compressedList.size());
        assertTrue(compressedList.contains(startConnectorMoveCommand));
        assertTrue(compressedList.contains(endConnectorMoveCommand));
        assertTrue(compressedList.contains(startConnectorSnapCommand));
        assertTrue(compressedList.contains(endConnectorSnapCommand));
    }

    /**
     * Test that when a connector was snapped to another element, that element's commands are saved
     * as if it still existed.
     */
    @Test
    void testDeletedElementsSavedWhenLastSnapped() {
        var compressor = new CommandListCompressor(diagramModel, elementIdManager);
        var testId = 12L;
        var otherElementId = 13L;
        var otherElementId2 = 14L;
        var otherElementId3 = 15L;
        when(elementIdManager.getUsedIds()).thenReturn(new LinkedList<>(Arrays.asList(testId)));
        when(elementIdManager.getElementControllerById(12L)).thenReturn(connectorElementController);
        List<SnapHandle> snappedHandlesList = new LinkedList<>(Arrays.asList(snapHandle, snapHandle2));
        when(connectorElementController.getLastSnappedHandles()).thenReturn(snappedHandlesList);
        when(snapHandle.getParentElementId()).thenReturn(otherElementId);
        when(snapHandle2.getParentElementId()).thenReturn(otherElementId2);

        List<Command<?>> commandList = new LinkedList<>();
        commandList.add(new EditTextCommand(new EditTextCommandArgs("new text", otherElementId), elementIdManager));
        var editCmd = new EditTextCommand(new EditTextCommandArgs("newer text", otherElementId), elementIdManager);
        commandList.add(editCmd);
        commandList.add(new RemoveCommand(new RemoveCommandArgs(new long[] {otherElementId2}), diagramModel, elementIdManager));
        var removeCmd = new RemoveCommand(new RemoveCommandArgs(new long[] {otherElementId2}), diagramModel, elementIdManager);
        commandList.add(removeCmd);
        var removeCmd2 = new RemoveCommand(new RemoveCommandArgs(new long[] {otherElementId3}), diagramModel, elementIdManager);
        commandList.add(removeCmd2);
        var startMoveArgs = new ConnectorMovePointCommandArgs(
                true, 0, 1, 2, 3, testId
        );
        var startConnectorMoveCommand = new ConnectorMovePointCommand(startMoveArgs, elementIdManager);
        commandList.add(new ConnectorMovePointCommand(startMoveArgs, elementIdManager));
        commandList.add(startConnectorMoveCommand);

        var compressedCommandList = compressor.compressCommandList(commandList);
        assertEquals(3, compressedCommandList.size());
        assertTrue(compressedCommandList.contains(editCmd));
        assertTrue(compressedCommandList.contains(removeCmd));
        assertTrue(compressedCommandList.contains(startConnectorMoveCommand));
        assertFalse(compressedCommandList.contains(removeCmd2));
    }
}
