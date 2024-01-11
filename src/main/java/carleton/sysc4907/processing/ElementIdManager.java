package carleton.sysc4907.processing;

import carleton.sysc4907.EditingAreaProvider;
import carleton.sysc4907.model.SessionModel;
import javafx.scene.Node;
import javafx.scene.Parent;

import java.util.Random;

/**
 * A class that manages the active editing area's elements, allowing searches for elements via their ID.
 * Note that elements in this case may represent DiagramElements, or other JavaFX entities such as previews, or
 * sub-elements of DiagramElements.
 * This is different from the DiagramModel, which manages only DiagramElements for the purposes of selecting them,
 * saving them, etc.
 */
public class ElementIdManager {

    private final SessionModel sessionModel;

    public ElementIdManager(SessionModel sessionModel) {
        this.sessionModel = sessionModel;
    }

    /**
     * Returns a new ID that can be allocated to a newly created element or sub-element. This ID
     * is guaranteed to not be used yet on any element currently in the diagram.
     * @return a new unique ID for the element
     */
    public Long getNewId() {
        Random rng = new Random();
        long id;
        do {
            id = (rng.nextLong() >> 8) << 8; // The shifts will unset the last 8 bits, so we can use them for user ID
            id = id | (sessionModel.getLocalUser().getId() & 0xFF); // Set the last 8 bits to the last 8 bits of the user's ID
        } while (existsWithId(id)); // Chance of collisions is low enough that one attempt is practically always enough
        return id;
    }

    /**
     * Returns a new ID that can be allocated to a newly created element with `length` sub-elements. This ID
     * is guaranteed to not be used yet on any element currently in the diagram, and the following `length` IDs are
     * also guaranteed to not be used yet, so they can be applied to sub-elements sequentially.
     * @param length the number of IDs past the first that need to be reserved for sub-elements
     * @return a new unique ID for the element
     */
    public Long getNewIdRange(int length) {
        long id = 0L;
        boolean foundId = false;
        while (!foundId) {
            id = getNewId();
            if (Long.MAX_VALUE - id <= length * 0xFFL) continue; // Avoid overflows
            boolean noIdConflicts = true;
            for (int i = 1; i <= length; i++) {
                if (existsWithId(getNextId(id, i))) {
                    noIdConflicts = false;
                }
            }
            if (noIdConflicts) foundId = true;
        }
        return id;
    }

    /**
     * Returns the next ID for the same user. Used for getting consecutive IDs for sub-elements.
     * @param id the previous ID
     * @param steps the number of "next" steps to take from the original ID
     * @return the new ID
     */
    public Long getNextId(long id, int steps) {
        return id + steps * 0x100L;
    }

    /**
     * Checks whether there is already an element in the diagram with the given ID.
     * @param id the ID to check
     * @return true if such an element exists, false otherwise
     */
    public boolean existsWithId(Long id) {
        return getElementById(id) != null;
    }

    /**
     * Gets the element in the diagram with the given ID, if it exists.
     * @param id the ID to get the element for
     * @return the element as a Node if it exists, or null if there is no element with the ID
     */
    public Node getElementById(Long id) {
        Parent parent = EditingAreaProvider.getEditingArea();
        return getElementByIdInParent(parent, id);
    }

    private Node getElementByIdInParent(Parent parent, Long id) {
        for (Node n : parent.getChildrenUnmodifiable()) {
            if (id.equals(n.getUserData())) {
                return n;
            }
            // Recursively search inside non-leaf nodes
            if (n instanceof Parent) {
                Node resultInNode = getElementByIdInParent((Parent) n, id);
                if (resultInNode != null) return resultInNode;
            }
        }
        return null;
    }
}
