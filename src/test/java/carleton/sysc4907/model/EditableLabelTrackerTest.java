package carleton.sysc4907.model;

import javafx.beans.property.ObjectProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class EditableLabelTrackerTest {
    public EditableLabelTracker editableLabelTracker;

    @BeforeEach
    public void setup() {
        editableLabelTracker = new EditableLabelTracker();
    }

    @Test
    public void setGetIdLastEditedLabelTest() {
        long expectedId = 1234;
        editableLabelTracker.setIdLastEditedLabel(expectedId);
        long actualId = editableLabelTracker.getIdLastEditedLabel();
        assertEquals(expectedId, actualId);
    }

    @Test
    public void getIdLastEditedLabelProperty() {
        ObjectProperty<Long> property = editableLabelTracker.idLastEditedLabelProperty();
        assertNotNull(property);
    }
}
