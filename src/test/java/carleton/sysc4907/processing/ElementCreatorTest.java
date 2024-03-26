package carleton.sysc4907.processing;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.view.DiagramElement;
import javafx.collections.FXCollections;
import javafx.scene.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ElementCreatorTest {

    private final String TEST_FILE_PATH = "carleton/sysc4907/processing/test_templates.xml";
    @Mock
    private DependencyInjector mockElementInjector;

    @Mock
    private ElementIdManager mockElementIdManager;

    @Mock
    private DiagramElement element1;

    @Mock
    private DiagramElement element2;

    @Mock
    private DiagramElement element3;

    @Test
    public void resolveElementTemplates() throws Exception {
        // Test that the constructor does not throw an error for a valid XML file
        ElementCreator elementCreator = new ElementCreator(mockElementInjector, TEST_FILE_PATH, mockElementIdManager);

        assertEquals(
                "/carleton/sysc4907/view/element/Rectangle.fxml",
                elementCreator.resolveTypeToTemplate("rectangle"));
        assertEquals(
                "/carleton/sysc4907/view/element/UmlComment.fxml",
                elementCreator.resolveTypeToTemplate("uml-comment"));
        assertNull(elementCreator.resolveTypeToTemplate("nonexistent"));
    }

    @Test
    public void getSubElementCounts() throws Exception {
        // Test that the constructor does not throw an error for a valid XML file
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/Rectangle.fxml")).thenReturn(element1);
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/UmlComment.fxml")).thenReturn(element2);

        List<Node> nodes = new LinkedList<>();
        nodes.add(element3);

        Mockito.when(element1.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(nodes));
        Mockito.when(element2.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
        Mockito.when(element3.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
        Mockito.when(element3.getUserData()).thenReturn("0");
        ElementCreator elementCreator = new ElementCreator(mockElementInjector, TEST_FILE_PATH, mockElementIdManager);

        assertEquals(
                1,
                elementCreator.countIdSubElements("rectangle"));
        assertEquals(
                0,
                elementCreator.countIdSubElements("uml-comment"));
        assertThrows(IllegalArgumentException.class, () -> elementCreator.countIdSubElements("nonexistent"));
    }

    @Test
    public void createElementsWithoutSubElements() throws Exception {
        // Test that the constructor does not throw an error for a valid XML file
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/Rectangle.fxml")).thenReturn(element1);
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/UmlComment.fxml")).thenReturn(element2);
        Mockito.when(element1.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
        Mockito.when(element2.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
        ElementCreator elementCreator = new ElementCreator(mockElementInjector, TEST_FILE_PATH, mockElementIdManager);

        assertEquals(element1, elementCreator.create("rectangle", 0L, true));
        assertEquals(element2, elementCreator.create("uml-comment", 0L, true));
        Mockito.verify(mockElementInjector).load("/carleton/sysc4907/view/element/Rectangle.fxml");
        Mockito.verify(mockElementInjector).load("/carleton/sysc4907/view/element/UmlComment.fxml");
    }

    @Test
    public void createElementsWithSubElements() throws Exception {
        // Test that the constructor does not throw an error for a valid XML file
        List<Node> nodes = new LinkedList<>();
        nodes.add(element3);

        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/Rectangle.fxml")).thenReturn(element1);
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/UmlComment.fxml")).thenReturn(element2);
        Mockito.when(element1.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(nodes));
        Mockito.when(element2.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
        Mockito.when(element3.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
        Mockito.when(element3.getUserData()).thenReturn("0");
        Mockito.when(mockElementIdManager.getNextId(0L, 1)).thenReturn(1L);
        ElementCreator elementCreator = new ElementCreator(mockElementInjector, TEST_FILE_PATH, mockElementIdManager);

        assertEquals(element1, elementCreator.create("rectangle", 0L, true));
        assertEquals(element2, elementCreator.create("uml-comment", 0L, true));
        Mockito.verify(element1).setUserData(0L);
        Mockito.verify(element3).setUserData(1L);
        Mockito.verify(element2).setUserData(0L);
        Mockito.verify(mockElementInjector).load("/carleton/sysc4907/view/element/Rectangle.fxml");
        Mockito.verify(mockElementInjector).load("/carleton/sysc4907/view/element/UmlComment.fxml");
    }
}
