package carleton.sysc4907.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javax.script.Bindings;
import java.util.LinkedList;

/**
 * Model for the UML diagram
 */
public class DiagramModel {

    private final ObservableList<DiagramElement> elements;

    private final ObjectProperty<DiagramElement> selectedElement = new SimpleObjectProperty<>();

    private final BooleanProperty isElementSelected = new SimpleBooleanProperty();

    public DiagramModel() {
        elements = FXCollections.observableList(new LinkedList<>());
        selectedElement.set(null);
        isElementSelected.bind(selectedElement.isNull());
    }

    /**
     * Gets all elements currently in this diagram.
     * @return an ObservableList containing all diagram elements
     */
    public ObservableList<DiagramElement> getElements() {
        return elements;
    }

    /**
     * Gets the currently selected element.
     * @return the selected DiagramElement, or null if none selected
     */
    public ObjectProperty<DiagramElement> getSelectedElementProperty() {
        return selectedElement;
    }

    public DiagramElement getSelectedElement() {
        return selectedElement.get();
    }

    public void setSelectedElement(DiagramElement selectedElement) {
        this.selectedElement.set(selectedElement);
        System.out.println("Selected element set: " + selectedElement);
    }

    public BooleanProperty getIsElementSelectedProperty() {
        return isElementSelected;
    }
}
