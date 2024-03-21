package carleton.sysc4907.model;

import carleton.sysc4907.view.SnapHandle;
import javafx.geometry.Bounds;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SnapHandleProviderTest {

    private SnapHandleProvider snapHandleProvider;

    @Mock
    private SnapHandle handle1;
    @Mock
    private SnapHandle handle2;
    @Mock
    private SnapHandle handle3;

    @Mock
    private Bounds bounds1;
    @Mock
    private Bounds bounds2;

    @BeforeEach
    void setup() {
        this.snapHandleProvider = new SnapHandleProvider(); // initialize a new provider with no handles set
    }

    @Test
    void testGetSingleton() {
        var singleton = SnapHandleProvider.getSingleton();
        var singleton2 = SnapHandleProvider.getSingleton();
        assertEquals(singleton, singleton2);
    }

    @Test
    void testAddSnapHandlesToList() {
        assertTrue(snapHandleProvider.getSnapHandleList().isEmpty());
        snapHandleProvider.getSnapHandleList().add(handle1);
        snapHandleProvider.getSnapHandleList().add(handle2);
        snapHandleProvider.getSnapHandleList().add(handle3);
        assertEquals(3, snapHandleProvider.getSnapHandleList().size());
    }

    @Test
    void testGetHandleAtPositionEmptyList() {
        assertNull(snapHandleProvider.getSnapHandleAtPosition(0, 0, new LinkedList<>()));
    }

    @Test
    void testGetHandleAtPositionFound() {
        // Make all handles active
        when(handle1.isHandleVisible()).thenReturn(true);
        when(handle2.isHandleVisible()).thenReturn(true);
        when(handle3.isHandleVisible()).thenReturn(true);
        // Make all handles return bounds, bounds2 will contain the point but not bounds1
        when(handle1.getBoundsInLocal()).thenReturn(bounds1);
        when(handle1.localToScene(any(Bounds.class))).thenReturn(bounds1);
        when(handle2.getBoundsInLocal()).thenReturn(bounds1);
        when(handle2.localToScene(any(Bounds.class))).thenReturn(bounds1);
        when(handle3.getBoundsInLocal()).thenReturn(bounds2); // handle 3 will contain the point
        when(handle3.localToScene(any(Bounds.class))).thenReturn(bounds2);
        when(bounds1.contains(1, 2)).thenReturn(false);
        when(bounds2.contains(1, 2)).thenReturn(true);

        snapHandleProvider.getSnapHandleList().add(handle1);
        snapHandleProvider.getSnapHandleList().add(handle2);
        snapHandleProvider.getSnapHandleList().add(handle3);

        assertEquals(handle3, snapHandleProvider.getSnapHandleAtPosition(1, 2, new LinkedList<>()));
    }

    @Test
    void testGetHandleAtPositionFoundButExcluded() {
        // Make all handles active
        when(handle1.isHandleVisible()).thenReturn(true);
        when(handle2.isHandleVisible()).thenReturn(true);
        // Make all handles return bounds, bounds2 will contain the point but not bounds1
        when(handle1.getBoundsInLocal()).thenReturn(bounds1);
        when(handle1.localToScene(any(Bounds.class))).thenReturn(bounds1);
        when(handle2.getBoundsInLocal()).thenReturn(bounds1);
        when(handle2.localToScene(any(Bounds.class))).thenReturn(bounds1);
        when(bounds1.contains(1, 2)).thenReturn(false);

        snapHandleProvider.getSnapHandleList().add(handle1);
        snapHandleProvider.getSnapHandleList().add(handle2);
        snapHandleProvider.getSnapHandleList().add(handle3);

        List<SnapHandle> excludeList = new LinkedList<>();
        excludeList.add(handle3);
        assertNull(snapHandleProvider.getSnapHandleAtPosition(1, 2, excludeList));
    }

    @Test
    void testGetHandleAtPositionFoundButInactive() {
        // Make all handles active
        when(handle1.isHandleVisible()).thenReturn(true);
        when(handle2.isHandleVisible()).thenReturn(true);
        when(handle3.isHandleVisible()).thenReturn(false);
        // Make all handles return bounds, bounds2 will contain the point but not bounds1
        when(handle1.getBoundsInLocal()).thenReturn(bounds1);
        when(handle1.localToScene(any(Bounds.class))).thenReturn(bounds1);
        when(handle2.getBoundsInLocal()).thenReturn(bounds1);
        when(handle2.localToScene(any(Bounds.class))).thenReturn(bounds1);
        when(handle3.getBoundsInLocal()).thenReturn(bounds2); // handle 3 will contain the point
        when(handle3.localToScene(any(Bounds.class))).thenReturn(bounds2);
        when(bounds1.contains(1, 2)).thenReturn(false);
        lenient().when(bounds2.contains(1, 2)).thenReturn(true); // not called intentionally due to short circuit, could change

        snapHandleProvider.getSnapHandleList().add(handle1);
        snapHandleProvider.getSnapHandleList().add(handle2);
        snapHandleProvider.getSnapHandleList().add(handle3);

        assertNull(snapHandleProvider.getSnapHandleAtPosition(1, 2, new LinkedList<>()));
    }

    @ParameterizedTest
    @ValueSource(ints = {0, 1})
    void testMakeAllHandlesVisible(int makeVisibleInt) {
        boolean makeVisible = makeVisibleInt == 1; // our version of JUnit doesn't support booleans in params yet

        snapHandleProvider.getSnapHandleList().add(handle1);
        snapHandleProvider.getSnapHandleList().add(handle2);
        snapHandleProvider.getSnapHandleList().add(handle3);

        snapHandleProvider.setAllHandlesVisible(makeVisible);
        verify(handle1).setHandleVisible(makeVisible);
        verify(handle2).setHandleVisible(makeVisible);
        verify(handle3).setHandleVisible(makeVisible);
    }
}
