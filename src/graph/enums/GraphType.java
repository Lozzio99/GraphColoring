package graph.enums;

public enum GraphType {
    NULL_GRAPH("Null Graph"),
    COMPLETE_GRAPH("Complete Graph"),
    STAR_GRAPH("Star Graph"),
    BIPARTITE_GRAPH("Bipartite Graph"),
    CYCLE_GRAPH("Cycle Graph"),
    WHEEL_GRAPH("Wheel Graph"),
    TREE_GRAPH("Tree Graph");

    private String name;

    GraphType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
