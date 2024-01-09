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
import static org.junit.jupiter.api.Assertions.assertFalse;
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

    @Test
    public void testGetNewIdRange() {
        SessionModel sessionModel = new SessionModel("12345678ABCD", new User(1, "TEST", PermissionLevel.HOST));
        Pane editingArea = new Pane();
        EditingAreaProvider.init(editingArea);
        ElementIdManager elementIdManager = new ElementIdManager(sessionModel);
        var id = elementIdManager.getNewIdRange(100);
        System.out.println(id);
        var expectedLastBits = id & 0xFF;
        assertEquals(expectedLastBits, id & 0xFF);
        for (int i = 1; i <= 100; i++) {
            System.out.println(elementIdManager.getNextId(id, i));
            System.out.println(String.format("0x%08X", elementIdManager.getNextId(id, i)));
            assertFalse(elementIdManager.existsWithId(elementIdManager.getNextId(id, i)));
        }
    }
}
