package graph.enums;

public enum LowerBound {
    TOMITA("Tomita"),
    MAXIMUM_CLIQUE("Maximum Clique");

    private String name;

    LowerBound(String name) {
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
