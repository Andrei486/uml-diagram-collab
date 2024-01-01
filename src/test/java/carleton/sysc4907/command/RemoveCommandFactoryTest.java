package carleton.sysc4907.command;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.command.args.AddCommandArgs;
import carleton.sysc4907.command.args.MoveCommandArgs;
import carleton.sysc4907.command.args.RemoveCommandArgs;
import carleton.sysc4907.model.DiagramModel;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;

public class RemoveCommandFactoryTest {

    @Test
    void createCommand() {
        List<DiagramElement> elems = new ArrayList<>();
        RemoveCommandArgs args = new RemoveCommandArgs(elems);
        DiagramModel diagramModel = new DiagramModel();
        RemoveCommandFactory factory = new RemoveCommandFactory(diagramModel);

        var command = factory.create(args);

        assertEquals(RemoveCommand.class, command.getClass());
    }
}
