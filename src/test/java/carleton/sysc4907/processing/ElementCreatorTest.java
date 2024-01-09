package carleton.sysc4907.processing;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.view.DiagramElement;
import javafx.collections.FXCollections;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ElementCreatorTest {

    private final String TEST_FILE_PATH = "/carleton/sysc4907/model/templates.xml";
    @Mock
    private DependencyInjector mockElementInjector;

    @Mock
    private ElementIdManager mockElementIdManager;

    @Mock
    private DiagramElement element1;

    @Mock
    private DiagramElement element2;

    @Test
    public void resolveElementTemplates() throws Exception {
        // Test that the constructor does not throw an error for a valid XML file
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/Rectangle.fxml")).thenReturn(element1);
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/UmlComment.fxml")).thenReturn(element2);
        Mockito.when(element1.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
        Mockito.when(element2.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
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
    public void createElements() throws Exception {
        // Test that the constructor does not throw an error for a valid XML file
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/Rectangle.fxml")).thenReturn(element1);
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/UmlComment.fxml")).thenReturn(element2);
        Mockito.when(element1.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
        Mockito.when(element2.getChildrenUnmodifiable()).thenReturn(FXCollections.observableList(new LinkedList<>()));
        ElementCreator elementCreator = new ElementCreator(mockElementInjector, TEST_FILE_PATH, mockElementIdManager);

        assertEquals(element1, elementCreator.create("rectangle", 0L, false));
        assertEquals(element2, elementCreator.create("uml-comment", 0L, false));
        Mockito.verify(mockElementInjector, Mockito.times(2)).load("/carleton/sysc4907/view/element/Rectangle.fxml");
        Mockito.verify(mockElementInjector, Mockito.times(2)).load("/carleton/sysc4907/view/element/UmlComment.fxml");
    }
}
