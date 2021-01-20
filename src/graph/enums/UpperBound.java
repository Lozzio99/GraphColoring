package graph.enums;

public enum UpperBound {
    GREEDY("Greedy"),
    WELSH_POWELL("Welsh-Powell");

    private String name;

    UpperBound(String name) {
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
