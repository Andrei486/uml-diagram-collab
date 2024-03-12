package carleton.sysc4907.controller.element.pathing;

/**
 * Factory for instantiating different types of pathing strategies, to prevent direct dependencies to strategies.
 */
public class PathingStrategyFactory {

    /**
     * Creates a new instance of the specified type of pathing strategy.
     * @param type the type of strategy to instantiate
     * @return the new pathing strategy
     */
    public PathingStrategy createPathingStrategy(PathType type) {
        switch (type) {
            case STRAIGHT -> {
                return new DirectPathStrategy();
            }
            case CURVED -> {
                return new CurvedPathStrategy();
            }
            case ORTHOGONAL -> {
                return new OrthogonalPathStrategy();
            }
            default -> {
                return null;
            }
        }
    }
}
