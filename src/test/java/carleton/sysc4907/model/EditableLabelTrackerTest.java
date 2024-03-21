package carleton.sysc4907.model;

import carleton.sysc4907.processing.ElementIdManager;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class EditableLabelTrackerTest {
    public EditableLabelTracker editableLabelTracker;

    private Label mockLabel;

    @Mock
    private ObservableList<String> mockStyleClass;

    @Mock
    private Font mockFont;

    @Mock
    public ElementIdManager mockElementIdManager;

    @BeforeEach
    public void setup() {
        editableLabelTracker = new EditableLabelTracker(mockElementIdManager);
    }

    @Test
    public void setGetIdLastEditedLabelTest() {
        //SETUP
        long expectedId = 1234;
        String fontFamily = "Arial";
        boolean isBolded = true;
        boolean isUnderlined = false;
        boolean isItalicized = true;
        double fontSize = 16D;

        mockLabel = mock(Label.class);
        Mockito.when(mockElementIdManager.getElementById(expectedId)).thenReturn(mockLabel);
        Mockito.when(mockLabel.getStyleClass()).thenReturn(mockStyleClass);
        Mockito.when(mockLabel.getFont()).thenReturn(mockFont);
        Mockito.when(mockFont.getSize()).thenReturn(fontSize);
        Mockito.when(mockFont.getFamily()).thenReturn(fontFamily);
        Mockito.when(mockStyleClass.contains("bolded")).thenReturn(isBolded);
        Mockito.when(mockStyleClass.contains("underlined")).thenReturn(isUnderlined);
        Mockito.when(mockStyleClass.contains("italicized")).thenReturn(isItalicized);

        //EXECUTE
        editableLabelTracker.setIdLastEditedLabel(expectedId);
        long actualId = editableLabelTracker.getIdLastEditedLabel();

        //ASSERT
        assertEquals(expectedId, actualId);
        assertEquals(isBolded, editableLabelTracker.getIsBoldProperty().get());
        assertEquals(isItalicized, editableLabelTracker.getIsItalicProperty().get());
        assertEquals(isUnderlined, editableLabelTracker.getIsUnderlinedProperty().get());
        assertEquals(isBolded, editableLabelTracker.getIsBoldProperty().get());
        assertEquals(fontFamily, editableLabelTracker.getFontFamilyProperty().get());
        assertEquals(fontSize, editableLabelTracker.getFontSizeProperty().get());
    }

    @Test
    public void getIdLastEditedLabelProperty() {
        var property = editableLabelTracker.idLastEditedLabelProperty();
        assertNotNull(property);
    }
}
