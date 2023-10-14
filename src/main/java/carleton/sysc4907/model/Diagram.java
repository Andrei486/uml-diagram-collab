package carleton.sysc4907.model;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Diagram {

    List<DiagramElement> elements;
    private static Diagram singleInstance = null;

    public Diagram() {
        elements = new LinkedList<>();
    }

    public static Diagram getSingleInstance() {
        if (singleInstance == null) {
            singleInstance = new Diagram();
        }
        return singleInstance;
    }
}
