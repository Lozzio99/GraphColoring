package graph.enums;

public enum Algorithm {
    ILS("Iterated Local Search"),
    RLF("Recursive-Large-First"),
    TABU("TABUCOL"),
    BACKTRACKING("Backtracking"),
    GREEDY("Greedy"),
    DSATUR("DSatur"),
    GA("genetic Algorithm");

    private String name;

    Algorithm(String name) {
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
