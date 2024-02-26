package carleton.sysc4907.processing;

import carleton.sysc4907.DiagramEditorLoader;
import carleton.sysc4907.command.AddCommand;
import carleton.sysc4907.command.CommandListCompressor;
import carleton.sysc4907.command.MoveCommand;
import carleton.sysc4907.command.RemoveCommand;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.ExecutedCommandList;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

@ExtendWith(MockitoExtension.class)
public class FileSaveLoadTest {

    @Mock
    private ElementIdManager mockElementIdManager;

    @Mock
    private ElementCreator mockElementCreator;

    @Mock
    private DiagramEditorLoader diagramEditorLoader;

    @Test
    public void testSaveAndLoadFromExistingTempFile() throws IOException {
        // Instantiate classes
        var mockIds = new LinkedList<Long>();
        mockIds.add(120L);
        Mockito.when(mockElementIdManager.getUsedIds()).thenReturn(mockIds);
        var diagramModel = new DiagramModel();
        var executedCommandList = new ExecutedCommandList();
        var commandListCompressor = new CommandListCompressor(diagramModel, mockElementIdManager);
        var fileSaver = new FileSaver(diagramModel, executedCommandList, commandListCompressor);
        var fileLoader = new FileLoader(diagramEditorLoader);
        // Create the temp file to save to
        File temp = File.createTempFile("temp", ".txt");
        temp.deleteOnExit();
        String filePath = temp.getPath();
        // Populate an executed command list
        var addCommand = new AddCommand(
                new AddCommandArgs("", 120L),
                diagramModel,
                mockElementCreator
        );
        var moveCommand = new MoveCommand(
                new MoveCommandArgs(0, 1, 100, 201, 120L),
                mockElementIdManager
        );
        var removeCommand = new RemoveCommand(
                new RemoveCommandArgs(new long[] {120L}),
                diagramModel,
                mockElementIdManager
        );
        executedCommandList.getCommandList().add(addCommand);
        executedCommandList.getCommandList().add(moveCommand);
        executedCommandList.getCommandList().add(removeCommand);
        diagramModel.setLoadedFilePath(filePath);

        // Perform test: save to file and then load
        fileSaver.save();
        Object[] deserialized = fileLoader.deserializeArgsFromFile(temp);

        // Compare the args to the original ones, they should be the same
        Assertions.assertEquals(addCommand.getArgs(), deserialized[0]);
        Assertions.assertEquals(moveCommand.getArgs(), deserialized[1]);
        Assertions.assertEquals(removeCommand.getArgs().elementIds()[0], ((RemoveCommandArgs) deserialized[2]).elementIds()[0]);
    }
}
