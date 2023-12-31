package carleton.sysc4907.processing;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.processing.ElementCreator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ElementCreatorTest {

    private final String TEST_FILE_PATH = "/carleton/sysc4907/model/templates.xml";
    @Mock
    private DependencyInjector mockElementInjector;

    @Test
    public void getElementTemplates() throws Exception {
        ElementCreator elementCreator = new ElementCreator(mockElementInjector, TEST_FILE_PATH);
    }
}
