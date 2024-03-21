package carleton.sysc4907.command;

import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.EditableLabelTracker;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.processing.ElementIdManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ChangeTextStyleCommandFactoryTest {

    ChangeTextStyleCommandFactory changeTextStyleCommandFactory;
    @Mock
    private ElementIdManager elementIdManager;

    @Mock
    private EditableLabelTracker editableLabelTracker;

    @Test
    public void createCommandTest() {
        ExecutedCommandList mockExecutedCommandList = Mockito.mock(ExecutedCommandList.class);
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.BOLD, "value", 12345L);
        Manager mockManager = Mockito.mock(Manager.class);
        MessageConstructor mockMessageConstructor = Mockito.mock(MessageConstructor.class);
        ChangeTextStyleCommandFactory factory = new ChangeTextStyleCommandFactory(
                elementIdManager,
                mockManager,
                mockExecutedCommandList,
                mockMessageConstructor,
                editableLabelTracker);

        var command = factory.create(args);

        assertEquals(ChangeTextStyleCommand.class, command.getClass());
    }
}
