package carleton.sysc4907.command;

import carleton.sysc4907.command.args.ChangeTextStyleCommandArgs;
import carleton.sysc4907.controller.element.EditableLabelController;
import carleton.sysc4907.model.EditableLabelTracker;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.text.Font;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ArgumentsSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class ChangeTextStyleCommandTest {

    private Label mockLabel;

    @Mock
    private Parent mockNode;

    @Mock
    private ObservableMap<Object, Object> mockProperties;

    @Mock
    private ElementIdManager elementIdManager;

    @Mock
    private EditableLabelTracker mockEditableLabelTracker;

    @Mock
    private EditableLabelController mockEditableLabelController;

    @Mock
    private ObservableList<String> mockStyleClass;

    @Mock
    private BooleanProperty mockBooleanProperty;

    @Mock
    private DoubleProperty mockDoubleProperty;

    @Mock
    private StringProperty mockStringProperty;

    @Mock
    private Font mockFont;

    private TextArea mockTextArea;


    @BeforeEach
    public void setup() {
        //mock a label, then the tests will make sure the right things were called
        //need the element ID manager to return the mocked label when getelembyid
        mockLabel = mock(Label.class);
        mockTextArea = mock(TextArea.class);

        //mandatory for all
        Mockito.when(elementIdManager.getElementById(anyLong())).thenReturn(mockLabel);
        Mockito.when(mockLabel.getParent()).thenReturn(mockNode);
        Mockito.when(mockNode.getProperties()).thenReturn(mockProperties);
        Mockito.when(mockProperties.get("controller")).thenReturn(mockEditableLabelController);
        Mockito.when(mockEditableLabelController.getEditableText()).thenReturn(mockTextArea);

    }

    @ParameterizedTest
    @ValueSource(ints =  {1, 0})
    public void testBoldCommand_trackerIdMatch(int intBoolean) {
        boolean makeBold = 1 == intBoolean;
        // SETUP
        Long id = 123L;
        Mockito.when(mockLabel.getStyleClass()).thenReturn(mockStyleClass);
        if (makeBold) {
            Mockito.when(mockStyleClass.add("bolded")).thenReturn(true);
        }
        else {
            Mockito.when(mockStyleClass.removeAll("bolded")).thenReturn(true);
        }
        Mockito.when(mockTextArea.getStyleClass()).thenReturn(mockStyleClass);

        Mockito.when(mockEditableLabelTracker.getIdLastEditedLabel()).thenReturn(id);
        Mockito.when(mockEditableLabelTracker.getIsBoldProperty()).thenReturn(mockBooleanProperty);
        Mockito.doNothing().when(mockBooleanProperty).set(makeBold);

        // EXECUTE
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.BOLD, makeBold, id);
        ChangeTextStyleCommand command = new ChangeTextStyleCommand(args, elementIdManager, mockEditableLabelTracker);
        command.execute();

        // VERIFY
        if (makeBold) {
            Mockito.verify(mockBooleanProperty).set(true);
            Mockito.verify(mockStyleClass, Mockito.times(2)).add("bolded");
        }
        else {
            Mockito.verify(mockStyleClass, Mockito.times(2)).removeAll("bolded");
        }
    }

    @ParameterizedTest
    @ValueSource(ints =  {1, 0})
    public void testBoldCommand_trackerIdNoMatch(int intBoolean) {
        boolean makeBold = 1 == intBoolean;
        // SETUP
        Long id = 123L;
        Long id2 = 321L;
        Mockito.when(mockLabel.getStyleClass()).thenReturn(mockStyleClass);
        if (makeBold) {
            Mockito.when(mockStyleClass.add("bolded")).thenReturn(true);
        }
        else {
            Mockito.when(mockStyleClass.removeAll("bolded")).thenReturn(true);
        }
        Mockito.when(mockTextArea.getStyleClass()).thenReturn(mockStyleClass);

        Mockito.when(mockEditableLabelTracker.getIdLastEditedLabel()).thenReturn(id);

        // EXECUTE
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.BOLD, makeBold, id2);
        ChangeTextStyleCommand command = new ChangeTextStyleCommand(args, elementIdManager, mockEditableLabelTracker);
        command.execute();

        // VERIFY
        if (makeBold) {
            //make sure the tracker values are NOT set
            Mockito.verify(mockBooleanProperty, never()).set(true);
            Mockito.verify(mockStyleClass, Mockito.times(2)).add("bolded");
        }
        else {
            Mockito.verify(mockStyleClass, Mockito.times(2)).removeAll("bolded");
        }
    }

    @ParameterizedTest
    @ValueSource(ints =  {1, 0})
    public void testItalicsCommand(int intBoolean) {
        boolean makeItalic = 1 == intBoolean;
        // SETUP
        Long id = 123L;
        Mockito.when(mockLabel.getStyleClass()).thenReturn(mockStyleClass);

        if (makeItalic) {
            Mockito.when(mockStyleClass.add("italicized")).thenReturn(true);
        } else {
            Mockito.when(mockStyleClass.removeAll("italicized")).thenReturn(true);
        }
        Mockito.when(mockTextArea.getStyleClass()).thenReturn(mockStyleClass);

        Mockito.when(mockEditableLabelTracker.getIdLastEditedLabel()).thenReturn(id);
        Mockito.when(mockEditableLabelTracker.getIsItalicProperty()).thenReturn(mockBooleanProperty);
        Mockito.doNothing().when(mockBooleanProperty).set(makeItalic);

        // EXECUTE
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.ITALICS, makeItalic, id);
        ChangeTextStyleCommand command = new ChangeTextStyleCommand(args, elementIdManager, mockEditableLabelTracker);
        command.execute();

        // VERIFY
        if (makeItalic) {
            Mockito.verify(mockBooleanProperty).set(true);
            Mockito.verify(mockStyleClass, Mockito.times(2)).add("italicized");
        }
        else {
            Mockito.verify(mockStyleClass, Mockito.times(2)).removeAll("italicized");
        }

    }

    @ParameterizedTest
    @ValueSource(ints =  {1, 0})
    public void testUnderlineCommand(int intBoolean) {
        boolean makeUnderlined = 1 == intBoolean;
        // SETUP
        Long id = 123L;
        Mockito.when(mockLabel.getStyleClass()).thenReturn(mockStyleClass);
        if (makeUnderlined) {
            Mockito.when(mockStyleClass.add("underlined")).thenReturn(true);
        }
        else {
            Mockito.when(mockStyleClass.removeAll("underlined")).thenReturn(true);
        }
        Mockito.when(mockTextArea.getStyleClass()).thenReturn(mockStyleClass);

        Mockito.when(mockEditableLabelTracker.getIdLastEditedLabel()).thenReturn(id);
        Mockito.when(mockEditableLabelTracker.getIsUnderlinedProperty()).thenReturn(mockBooleanProperty);
        Mockito.doNothing().when(mockBooleanProperty).set(makeUnderlined);

        // EXECUTE
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.UNDERLINE, makeUnderlined, id);
        ChangeTextStyleCommand command = new ChangeTextStyleCommand(args, elementIdManager, mockEditableLabelTracker);
        command.execute();

        // VERIFY
        if (makeUnderlined) {
            Mockito.verify(mockBooleanProperty).set(true);
            Mockito.verify(mockStyleClass, Mockito.times(2)).add("underlined");
        }
        else {
            Mockito.verify(mockStyleClass, Mockito.times(2)).removeAll("underlined");
        }

    }

    @Test
    public void testFontSizeCommand_regex() {
        Long id = 123L;
        double fontSize = 16D;
        String fontFamily = "Arial";
        String oldStyle = "-fx-font-family: \"" + fontFamily + "\"; -fx-font-size: " + 12 + ";";
        String expectedStyle = "-fx-font-family: \"" + fontFamily + "\"; -fx-font-size: " + fontSize + ";";
        Mockito.when(mockEditableLabelTracker.getIdLastEditedLabel()).thenReturn(id);
        Mockito.when(mockEditableLabelTracker.getFontSizeProperty()).thenReturn(mockDoubleProperty);
        Mockito.doNothing().when(mockDoubleProperty).set(fontSize);

        Mockito.when(mockLabel.getFont()).thenReturn(mockFont);
        Mockito.when(mockFont.getFamily()).thenReturn(fontFamily);
        Mockito.when(mockLabel.getStyle()).thenReturn(oldStyle);
        Mockito.doNothing().when(mockLabel).setStyle(expectedStyle);
        Mockito.doNothing().when(mockTextArea).setStyle(expectedStyle);

        // EXECUTE
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.SIZE, fontSize, id);
        ChangeTextStyleCommand command = new ChangeTextStyleCommand(args, elementIdManager, mockEditableLabelTracker);
        command.execute();

        // VERIFY
        verify(mockDoubleProperty).set(fontSize);
        verify(mockLabel).setStyle(expectedStyle);
        verify(mockTextArea).setStyle(expectedStyle);

    }

    @Test
    public void testFontSizeCommand_noRegex() {
        Long id = 123L;
        double fontSize = 16D;
        String fontFamily = "Arial";
        String oldStyle = "";
        String expectedStyle = "-fx-font-family: \"" + fontFamily + "\"; -fx-font-size: " + fontSize + ";";
        Mockito.when(mockEditableLabelTracker.getIdLastEditedLabel()).thenReturn(id);
        Mockito.when(mockEditableLabelTracker.getFontSizeProperty()).thenReturn(mockDoubleProperty);
        Mockito.doNothing().when(mockDoubleProperty).set(fontSize);

        Mockito.when(mockLabel.getFont()).thenReturn(mockFont);
        Mockito.when(mockFont.getFamily()).thenReturn(fontFamily);
        Mockito.when(mockLabel.getStyle()).thenReturn(oldStyle);
        Mockito.doNothing().when(mockLabel).setStyle(expectedStyle);
        Mockito.doNothing().when(mockTextArea).setStyle(expectedStyle);

        // EXECUTE
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.SIZE, fontSize, id);
        ChangeTextStyleCommand command = new ChangeTextStyleCommand(args, elementIdManager, mockEditableLabelTracker);
        command.execute();

        // VERIFY
        verify(mockDoubleProperty).set(fontSize);
        verify(mockLabel).setStyle(expectedStyle);
        verify(mockTextArea).setStyle(expectedStyle);

    }
    @Test
    public void testFontFamilyCommand_regex() {
        Long id = 123L;
        double fontSize = 16D;
        String fontFamily = "Arial";
        String oldStyle = "-fx-font-family: \"" + "Segoe UI" + "\"; -fx-font-size: " + fontSize + ";";
        String expectedStyle = "-fx-font-family: \"" + fontFamily + "\"; -fx-font-size: " + fontSize + ";";
        Mockito.when(mockEditableLabelTracker.getIdLastEditedLabel()).thenReturn(id);
        Mockito.when(mockEditableLabelTracker.getFontFamilyProperty()).thenReturn(mockStringProperty);
        Mockito.doNothing().when(mockStringProperty).set(fontFamily);

        Mockito.when(mockLabel.getFont()).thenReturn(mockFont);
        Mockito.when(mockFont.getSize()).thenReturn(fontSize);
        Mockito.when(mockLabel.getStyle()).thenReturn(oldStyle);
        Mockito.doNothing().when(mockLabel).setStyle(expectedStyle);
        Mockito.doNothing().when(mockTextArea).setStyle(expectedStyle);

        // EXECUTE
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.FONT_FAMILY, fontFamily, id);
        ChangeTextStyleCommand command = new ChangeTextStyleCommand(args, elementIdManager, mockEditableLabelTracker);
        command.execute();

        // VERIFY
        verify(mockStringProperty).set(fontFamily);
        verify(mockLabel).setStyle(expectedStyle);
        verify(mockTextArea).setStyle(expectedStyle);
    }

    @Test
    public void testFontFamilyCommand_noRegex() {
        Long id = 123L;
        double fontSize = 16D;
        String fontFamily = "Arial";
        String oldStyle = "";
        String expectedStyle = "-fx-font-family: \"" + fontFamily + "\"; -fx-font-size: " + fontSize + ";";
        Mockito.when(mockEditableLabelTracker.getIdLastEditedLabel()).thenReturn(id);
        Mockito.when(mockEditableLabelTracker.getFontFamilyProperty()).thenReturn(mockStringProperty);
        Mockito.doNothing().when(mockStringProperty).set(fontFamily);

        Mockito.when(mockLabel.getFont()).thenReturn(mockFont);
        Mockito.when(mockFont.getSize()).thenReturn(fontSize);
        Mockito.when(mockLabel.getStyle()).thenReturn(oldStyle);
        Mockito.doNothing().when(mockLabel).setStyle(expectedStyle);
        Mockito.doNothing().when(mockTextArea).setStyle(expectedStyle);

        // EXECUTE
        ChangeTextStyleCommandArgs args = new ChangeTextStyleCommandArgs(TextStyleProperty.FONT_FAMILY, fontFamily, id);
        ChangeTextStyleCommand command = new ChangeTextStyleCommand(args, elementIdManager, mockEditableLabelTracker);
        command.execute();

        // VERIFY
        verify(mockStringProperty).set(fontFamily);
        verify(mockLabel).setStyle(expectedStyle);
        verify(mockTextArea).setStyle(expectedStyle);
    }


}
