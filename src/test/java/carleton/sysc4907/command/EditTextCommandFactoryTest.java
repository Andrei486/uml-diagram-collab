package carleton.sysc4907.command;

import carleton.sysc4907.command.args.EditTextCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementIdManager;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class EditTextCommandFactoryTest {

    @Mock
    private ElementIdManager mockElementIdManager;
    @Mock
    private ExecutedCommandList mockExecutedCommandList;
    @Mock
    private MessageConstructor mockMessageConstructor;

    @Test
    void createCommand() {
        long testId = 12L;
        String testString = "Test";
        Manager mockManager = Mockito.mock(Manager.class);
        var args = new EditTextCommandArgs(testString, testId);
        EditTextCommandFactory factory = new EditTextCommandFactory(mockElementIdManager, mockManager, mockExecutedCommandList, mockMessageConstructor);

        var command = factory.create(args);

        assertEquals(EditTextCommand.class, command.getClass());
    }
}
