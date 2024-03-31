package carleton.sysc4907.ui.processing;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.processing.EditingAreaCoordinateProvider;
import javafx.geometry.Bounds;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.testfx.framework.junit5.ApplicationExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@ExtendWith(ApplicationExtension.class)
public class EditingAreaCoordinateProviderTest {

    @Mock
    private Scene mockScene;

    private ScrollPane mockScrollPane;

    @Mock
    private Bounds mockViewportBounds;

    @Mock
    private Pane mockEditingArea;

    @Mock
    private Bounds mockEditingAreaBounds;

    private EditingAreaCoordinateProvider coordinateProvider;

    @BeforeEach
    void setup() {
        mockScrollPane = Mockito.mock(ScrollPane.class);
        coordinateProvider = new EditingAreaCoordinateProvider(mockScene);
    }

    @Test
    void testGetCenterVisibleX() {
        when(mockScene.lookup("#scrollPane")).thenReturn(mockScrollPane);
        when(mockScrollPane.getViewportBounds()).thenReturn(mockViewportBounds);
        when(mockEditingArea.getBoundsInLocal()).thenReturn(mockEditingAreaBounds);
        when(mockViewportBounds.getWidth()).thenReturn(1000.0);
        when(mockEditingAreaBounds.getWidth()).thenReturn(5000.0);
        when(mockScrollPane.getHvalue()).thenReturn(0.1);
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);

            var result = coordinateProvider.getCenterVisibleX();
            assertEquals(900.0, result);
        }
    }

    @Test
    void testGetCenterVisibleY() {
        when(mockScene.lookup("#scrollPane")).thenReturn(mockScrollPane);
        when(mockScrollPane.getViewportBounds()).thenReturn(mockViewportBounds);
        when(mockEditingArea.getBoundsInLocal()).thenReturn(mockEditingAreaBounds);
        when(mockViewportBounds.getHeight()).thenReturn(300.0);
        when(mockEditingAreaBounds.getHeight()).thenReturn(600.0);
        when(mockScrollPane.getVvalue()).thenReturn(0.5);
        try (MockedStatic<EditingAreaProvider> utilities = Mockito.mockStatic(EditingAreaProvider.class)) {
            utilities.when(EditingAreaProvider::getEditingArea).thenReturn(mockEditingArea);

            var result = coordinateProvider.getCenterVisibleY();
            assertEquals(300.0, result);
        }
    }
}
