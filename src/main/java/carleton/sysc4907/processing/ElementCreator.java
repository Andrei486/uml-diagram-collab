package carleton.sysc4907.processing;

import carleton.sysc4907.DependencyInjector;
import carleton.sysc4907.view.DiagramElement;
import javafx.scene.Node;
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
import java.util.LinkedList;
import java.util.List;

/**
 * Class responsible for creating diagram elements using FXML templates.
 */
public class ElementCreator {

    private final DependencyInjector elementInjector;
    private final ElementIdManager elementIdManager;
    private final String templateFilePath;

    private final HashMap<String, String> typeTemplateMap;
    private final HashMap<String, Integer> subElementCountMap;

    /**
     * Constructs a new ElementCreator.
     * @param elementInjector the DependencyInjector to use for creating controllers
     * @param templateFilePath the XML file that contains mappings from element type to FXML template
     */
    public ElementCreator(DependencyInjector elementInjector, String templateFilePath, ElementIdManager elementIdManager) throws
            ParserConfigurationException,
            IOException,
            SAXException,
            URISyntaxException {
        this.elementInjector = elementInjector;
        this.templateFilePath = templateFilePath;
        this.elementIdManager = elementIdManager;
        this.typeTemplateMap = new HashMap<>();
        this.subElementCountMap = new HashMap<>();
        populateTypeTemplateMap();
    }

    /**
     * Creates a new diagram element of the given type.
     * @param type the type of diagram element to create
     * @param elementId the ID to assign to the element
     * @param addSubElementIds true if IDs should also be assigned to sub-elements, false otherwise
     * @return the created diagram element, or null if none could be created
     */
    public DiagramElement create(String type, long elementId, boolean addSubElementIds) {
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
        if (addSubElementIds) addSubElementIds(element, elementId);
        return element;
    }

    private void addSubElementIds(Parent parent, long baseId) {
        List<Node> subElements = getSubElements(parent);
        int i = 1;
        for (Node sub : subElements) {
            sub.setUserData(elementIdManager.getNextId(baseId, i));
            i++;
        }
    }

    /**
     * Populates the type-template map using the file path provided in the constructor.
     */
    private void populateTypeTemplateMap() throws ParserConfigurationException, IOException, SAXException, URISyntaxException {
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        var stream = ElementCreator.class.getClassLoader().getResourceAsStream(templateFilePath);
        Document doc = builder.parse(stream);
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

    private void populateSubElementCountMap() {
        for (String type : typeTemplateMap.keySet()) {
            var element = create(type, 0L, false);
            subElementCountMap.put(type, getSubElements(element).size());
        }
    }

    /**
     * Gets the file path of the FXML template associated with the given type of element.
     * @param type name of the element type
     * @return file path of the template used to create elements of the given type
     */
    public String resolveTypeToTemplate(String type) {
        return this.typeTemplateMap.get(type);
    }

    /**
     * Gets all registered element types that this object can create.
     * @return collection containing all valid element types
     */
    public Collection<String> getRegisteredTypes() {
        return this.typeTemplateMap.keySet();
    }

    /**
     * Gets the number of sub-elements in an element that need to be assigned sub-elements.
     * @param type the type of diagram element to check for
     * @return the number of sub-elements that need IDs, never includes the parent
     */
    public int countIdSubElements(String type) {
        if (this.subElementCountMap.isEmpty()) populateSubElementCountMap();
        var result = this.subElementCountMap.get(type);
        if (result == null) throw new IllegalArgumentException(type + " is not a recognized element type");
        return result;
    }

    private List<Node> getSubElements(Parent parent) {
        List<Node> subElements = new LinkedList<>();
        for (Node n : parent.getChildrenUnmodifiable()) {
            if ("0".equals(n.getUserData())) {
                subElements.add(n);
            }
            // Recursively search inside non-leaf nodes
            if (n instanceof Parent) {
                subElements.addAll(getSubElements((Parent) n));
            }
        }
        return subElements;
    }
}
