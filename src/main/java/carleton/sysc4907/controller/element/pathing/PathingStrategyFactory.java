package carleton.sysc4907.controller.element.pathing;

public class PathingStrategyFactory {

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
