package carleton.sysc4907.processing;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.model.PermissionLevel;
import carleton.sysc4907.model.SessionModel;
import carleton.sysc4907.model.User;
import javafx.collections.FXCollections;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

public class ElementIdManagerFunctionalTest {

    @Test
    public void testGetNewId() {
        SessionModel sessionModel = new SessionModel("12345678ABCD", new User(1, "TEST", PermissionLevel.HOST));
        Pane editingArea = new Pane();
        EditingAreaProvider.init(editingArea);
        ElementIdManager elementIdManager = new ElementIdManager(sessionModel);
        var id = elementIdManager.getNewId();
        var expectedLastBits = id & 0xFF;
        assertEquals(expectedLastBits, id & 0xFF);
    }
}
