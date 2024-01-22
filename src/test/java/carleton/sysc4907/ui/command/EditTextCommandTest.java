package carleton.sysc4907.ui.command;

import carleton.sysc4907.command.EditTextCommand;
import carleton.sysc4907.command.args.EditTextCommandArgs;
import carleton.sysc4907.processing.ElementIdManager;
import javafx.scene.control.Label;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class) // required so we can create or mock Labels
public class EditTextCommandTest {

    private Label mockLabel;
    @Mock
    private ElementIdManager mockElementIdManager;

    @Test
    void executeMoves() {
        long testId = 12L;
        String testString = "Test";
        mockLabel = mock(Label.class);
        doNothing().when(mockLabel).setText(testString);
        when(mockElementIdManager.getElementById(testId)).thenReturn(mockLabel);

        var args = new EditTextCommandArgs(testString, testId);
        var command = new EditTextCommand(args, mockElementIdManager);

        command.execute();

        verify(mockLabel).setText(testString);
    }
}
