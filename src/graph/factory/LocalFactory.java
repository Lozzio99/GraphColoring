package graph.factory;

import graph.interfaces.GraphRepositoryInterface;

public class LocalFactory implements FactoryInterface {
    private GraphRepositoryInterface graphRepository;

    public LocalFactory(GraphRepositoryInterface graphRepository) {
        this.graphRepository = graphRepository;
    }

    @Override
    public GraphRepositoryInterface getGraphRepository() {
        return graphRepository;
    }
}
