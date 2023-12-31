package carleton.sysc4907.command;
import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;
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
        DependencyInjector mockElementInjector = Mockito.mock(DependencyInjector.class);
        AddCommandArgs args = new AddCommandArgs(fxmlPath);
        AddCommandFactory factory = new AddCommandFactory(diagramModel, mockElementInjector);

        var command = factory.create(args);

        assertEquals(AddCommand.class, command.getClass());
    }
}
