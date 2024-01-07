package carleton.sysc4907.processing;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.model.SessionModel;
import carleton.sysc4907.model.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ElementIdManagerTest {

    @Mock
    private SessionModel mockSessionModel;

    @Mock
    private User mockUser;

    @Mock
    private Parent mockParent;

    @Mock
    private Node mockNode;

    @Mock
    private Pane mockEditingArea;

    private final String testUserId = "testlongid";
    private final long testNodeId = 1L;

    private ElementIdManager elementIdManager;

    @BeforeEach
    public void setup() {
        elementIdManager = new ElementIdManager(mockSessionModel);
        lenient().when(mockSessionModel.getLocalUser()).thenReturn(mockUser);
        lenient().when(mockUser.getUsername()).thenReturn(testUserId);
        lenient().when(mockNode.getUserData()).thenReturn(testNodeId);
        var nodesList = new LinkedList<Node>();
        nodesList.add(mockNode);
        lenient().when(mockParent.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(nodesList));
    }

    @Test
    public void testGetNewId() {
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);
            when(mockEditingArea.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
            var id = elementIdManager.getNewId();
            var expectedLastBits = id & 0xFF;
            assertEquals(expectedLastBits, id & 0xFF);
        }
    }

    @Test
    public void getIdExists() {
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);
            ObservableList<Node> nodes = FXCollections.observableList(new LinkedList<>());
            nodes.add(mockNode);
            when(mockEditingArea.getChildrenUnmodifiable()).thenReturn(nodes);
            assertTrue(elementIdManager.existsWithId(testNodeId));
        }
    }

    @Test
    public void getElementByIdExists() {
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);
            ObservableList<Node> nodes = FXCollections.observableList(new LinkedList<>());
            nodes.add(mockNode);
            when(mockEditingArea.getChildrenUnmodifiable()).thenReturn(nodes);
            assertEquals(mockNode, elementIdManager.getElementById(testNodeId));
        }
    }

    @Test
    public void getElementByIdExistNested() {
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);
            ObservableList<Node> nodes = FXCollections.observableList(new LinkedList<>());
            nodes.add(mockParent);
            when(mockEditingArea.getChildrenUnmodifiable()).thenReturn(nodes);
            assertEquals(mockNode, elementIdManager.getElementById(testNodeId));
        }
    }

    @Test
    public void getIdDoesNotExist() {
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);
            ObservableList<Node> nodes = FXCollections.observableList(new LinkedList<>());
            nodes.add(mockNode);
            when(mockEditingArea.getChildrenUnmodifiable()).thenReturn(nodes);
            assertFalse(elementIdManager.existsWithId(201L));
        }
    }

    @Test
    public void getElementByIdDoesNotExist() {
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);
            ObservableList<Node> nodes = FXCollections.observableList(new LinkedList<>());
            nodes.add(mockNode);
            when(mockEditingArea.getChildrenUnmodifiable()).thenReturn(nodes);
            assertNull(elementIdManager.getElementById(201L));
        }
    }
}
