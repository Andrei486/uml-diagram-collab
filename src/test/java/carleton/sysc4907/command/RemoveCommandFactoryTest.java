package carleton.sysc4907.command;

import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class RemoveCommandFactoryTest {

    @Mock
    private ElementIdManager mockElementIdManager;
    @Mock
    private ExecutedCommandList mockExecutedCommandList;
    @Mock
    private MessageConstructor messageConstructor;

    @Test
    void createCommand() {
        long[] ids = new long[0];
        RemoveCommandArgs args = new RemoveCommandArgs(ids);
        DiagramModel diagramModel = new DiagramModel();
        Manager mockManager = Mockito.mock(Manager.class);
        RemoveCommandFactory factory = new RemoveCommandFactory(diagramModel, mockElementIdManager, mockManager, mockExecutedCommandList, messageConstructor);

        var command = factory.create(args);

        assertEquals(RemoveCommand.class, command.getClass());
    }
}
