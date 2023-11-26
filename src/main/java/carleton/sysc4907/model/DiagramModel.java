package carleton.sysc4907.model;

import carleton.sysc4907.view.DiagramElement;
import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.LinkedList;

/**
 * Model for the UML diagram. Keeps track of all the elements and which ones are selected.
 */
public class DiagramModel {

    private final ObservableList<DiagramElement> elements;

    private final ObservableList<DiagramElement> selectedElements;

    private final BooleanProperty isElementSelected = new SimpleBooleanProperty();

    /**
     * Constructs a new DiagramModel with no elements.
     */
    public DiagramModel() {
        elements = FXCollections.observableList(new LinkedList<>());
        selectedElements = FXCollections.observableList(new LinkedList<>());
        ListProperty<DiagramElement> listProperty = new SimpleListProperty<>();
        listProperty.setValue(selectedElements);
        isElementSelected.bind(listProperty.emptyProperty().not());
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
     *
     * @return the selected DiagramElement, or null if none selected
     */
    public ObservableList<DiagramElement> getSelectedElements() {
        return selectedElements;
    }

    /**
     * Gets the property corresponding to whether an element is selected.
     * @return the ObservableValue describing whether an element is currently selected
     */
    public BooleanProperty getIsElementSelectedProperty() {
        return isElementSelected;
    }
}