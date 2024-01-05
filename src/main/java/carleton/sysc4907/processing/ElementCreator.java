package carleton.sysc4907.processing;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.Parent;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;

/**
 * Class responsible for creating diagram elements using FXML templates.
 */
public class ElementCreator {

    private final DependencyInjector elementInjector;
    private final String templateFilePath;

    private final HashMap<String, String> typeTemplateMap;

    /**
     * Constructs a new ElementCreator.
     * @param elementInjector the DependencyInjector to use for creating controllers
     * @param templateFilePath the XML file that contains mappings from element type to FXML template
     */
    public ElementCreator(DependencyInjector elementInjector, String templateFilePath) throws
            ParserConfigurationException,
            IOException,
            SAXException,
            URISyntaxException {
        this.elementInjector = elementInjector;
        this.templateFilePath = templateFilePath;
        this.typeTemplateMap = new HashMap<>();
        populateTypeTemplateMap();
    }

    /**
     * Creates a new diagram element of the given type.
     * @param type the type of diagram element to create
     * @param elementId the ID to assign to the element
     * @return the created diagram element, or null if none could be created
     */
    public DiagramElement create(String type, long elementId) {
        String path = resolveTypeToTemplate(type);
        if (path == null) { // Type not recognized
            return null;
        }
        Parent obj;
        try {
            obj = elementInjector.load(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DiagramElement element = (DiagramElement) obj;
        element.setUserData(elementId);
        return element;
    }

    /**
     * Populates the type-template map using the file path provided in the constructor.
     */
    private void populateTypeTemplateMap() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = builder.parse(new File(ElementCreator.class.getResource(this.templateFilePath).toURI()));
        doc.getDocumentElement().normalize();
        NodeList nodeList = doc.getElementsByTagName("template");
        for (int i = 0; i < nodeList.getLength(); i++) {
            var node = nodeList.item(i);
            var attributes = node.getAttributes();
            String type = attributes.getNamedItem("type").getNodeValue();
            String fxmlPath = attributes.getNamedItem("fxml").getNodeValue();
            this.typeTemplateMap.put(type, fxmlPath);
        }
    }

    /**
     * Gets the file path of the FXML template associated with the given type of element.
     * @param type name of the element type
     * @return file path of the template used to create elements of the given type
     */
    public String resolveTypeToTemplate(String type) {
        System.out.println(type + " " + this.typeTemplateMap.get(type));
        return this.typeTemplateMap.get(type);
    }

    /**
     * Gets all registered element types that this object can create.
     * @return collection containing all valid element types
     */
    public Collection<String> getRegisteredTypes() {
        return this.typeTemplateMap.keySet();
    }
}
