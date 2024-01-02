package carleton.sysc4907.processing;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.processing.ElementCreator;
import carleton.sysc4907.view.DiagramElement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class ElementCreatorTest {

    private final String TEST_FILE_PATH = "/carleton/sysc4907/model/templates.xml";
    @Mock
    private DependencyInjector mockElementInjector;

    @Mock
    private DiagramElement element1;

    @Mock
    private DiagramElement element2;

    @Test
    public void resolveElementTemplates() throws Exception {
        // Test that the constructor does not throw an error for a valid XML file
        ElementCreator elementCreator = new ElementCreator(mockElementInjector, TEST_FILE_PATH);

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
        ElementCreator elementCreator = new ElementCreator(mockElementInjector, TEST_FILE_PATH);
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/Rectangle.fxml")).thenReturn(element1);
        Mockito.when(mockElementInjector.load("/carleton/sysc4907/view/element/UmlComment.fxml")).thenReturn(element2);

        assertEquals(element1, elementCreator.create("rectangle"));
        assertEquals(element2, elementCreator.create("uml-comment"));
        Mockito.verify(mockElementInjector).load("/carleton/sysc4907/view/element/Rectangle.fxml");
        Mockito.verify(mockElementInjector).load("/carleton/sysc4907/view/element/UmlComment.fxml");
    }
}
