package carleton.sysc4907.command;
import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.communications.Manager;
import carleton.sysc4907.communications.MessageConstructor;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.model.ExecutedCommandList;
import carleton.sysc4907.processing.ElementCreator;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
public class AddCommandFactoryTest {
    @Test
    void createCommand() {
        String fxmlPath = "TestPath";
        DiagramModel diagramModel = new DiagramModel();
        ElementCreator mockElementCreator = Mockito.mock(ElementCreator.class);
        ExecutedCommandList mockExecutedCommandList = Mockito.mock(ExecutedCommandList.class);
        AddCommandArgs args = new AddCommandArgs(fxmlPath, 0);
        Manager mockManager = Mockito.mock(Manager.class);
        MessageConstructor mockConstructor = Mockito.mock(MessageConstructor.class);
        AddCommandFactory factory = new AddCommandFactory(diagramModel, mockElementCreator, mockManager, mockExecutedCommandList, mockConstructor);

        var command = factory.create(args);

        assertEquals(AddCommand.class, command.getClass());
    }
}
