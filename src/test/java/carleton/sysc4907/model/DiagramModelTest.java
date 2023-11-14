package carleton.sysc4907.model;

import carleton.sysc4907.view.DiagramElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DiagramModelTest {

    private DiagramModel model;

    private DiagramElement e1;
    private DiagramElement e2;

    @BeforeEach
    void setup() {
        model = new DiagramModel();
        e1 = new DiagramElement();
        e2 = new DiagramElement();
        model.getElements().add(e1);
        model.getElements().add(e2);
    }

    @Test
    void getElements() {
        List<DiagramElement> elements = model.getElements();
        assertEquals(2, elements.size());
        assertTrue(elements.contains(e1));
        assertTrue(elements.contains(e2));
    }

    @Test
    void getSelectedElements() {
        List<DiagramElement> elements = model.getSelectedElements();
        assertTrue(elements.isEmpty());
        model.getSelectedElements().add(e1);
        elements = model.getSelectedElements();
        assertEquals(1, elements.size());
        assertTrue(elements.contains(e1));
    }

    @Test
    void getIsElementSelectedProperty() {
        var isElementSelected = model.getIsElementSelectedProperty();
        assertFalse(isElementSelected.getValue());
        model.getSelectedElements().add(e1);
        assertTrue(isElementSelected.getValue());
        model.getSelectedElements().add(e2);
        assertTrue(isElementSelected.getValue());
    }
}