package graph.utils.tasks;

import graph.Main;
import graph.domain.Edge;
import graph.enums.GraphType;
import graph.exceptions.TimelimitExceededException;
import graph.utils.GraphCheck;
import graph.utils.ProgressBar;

import java.io.*;

public class ImportTask {
    public final static String COMMENT = "//";

    public static boolean execute(String fileName) throws IOException, TimelimitExceededException
    {
        ProgressBar progressBar = new ProgressBar();

        int n = -1;
        int m = -1;

        int currentBytes = 0;

        File file = new File(fileName);
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);

        progressBar.update(0, Math.toIntExact(file.length()));

        String record = new String();

        while ((record = br.readLine()) != null) {
            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException();
            }

            if (record.startsWith("//")) continue;
            break; // Saw a line that did not start with a comment -- time to start reading the data in!
        }

        if (record.startsWith("VERTICES = ")) {
            n = Integer.parseInt( record.substring(11) );
            Main.factory.getGraphRepository().setVertexCount(n);
        }

        record = br.readLine();

        if (record.startsWith("EDGES = ")) {
            m = Integer.parseInt(record.substring(8));

            Main.factory.getGraphRepository().setEdgeCount(m);
            Main.factory.getGraphRepository().setEdges(new Edge[m]);

            if (GraphCheck.isNullOrTrivialGraph(n, m)) {
                Main.factory.getGraphRepository().setGraphType(GraphType.NULL_GRAPH);
                progressBar.update(Math.toIntExact(file.length()), Math.toIntExact(file.length()));
                return true;
            }

            if (GraphCheck.isCompleteGraph(n, m)) {
                Main.factory.getGraphRepository().setGraphType(GraphType.COMPLETE_GRAPH);
                progressBar.update(Math.toIntExact(file.length()), Math.toIntExact(file.length()));
                return true;
            }
        }

        for (int d = 0; d < m; d++) {
            record = br.readLine();
            String[] data = record.split(" ");

            if (data.length != 2) {
                System.out.println("Error! Malformed edge line: "+record);
                System.exit(0);
            }

            Main.factory.getGraphRepository().getEdges()[d] = new Edge(Integer.parseInt(data[0]), Integer.parseInt(data[1]));

            currentBytes += record.getBytes().length;
            progressBar.update(currentBytes, Math.toIntExact(file.length()));

            if (Main.factory.getGraphRepository().getWatch().isExceeded()) {
                throw new TimelimitExceededException();
            }
        }

        progressBar.update(Math.toIntExact(file.length()), Math.toIntExact(file.length()));

        Main.factory.getGraphRepository().createAdjacentMatrix();

        if (GraphCheck.isStarGraph(Main.factory.getGraphRepository().getAdjacentMatrix(), Main.factory.getGraphRepository().getVertexCount())) {
            System.out.println("Star Graph");
            Main.factory.getGraphRepository().setGraphType(GraphType.STAR_GRAPH);
            return true;
        }

        if (GraphCheck.isBipartiteGraph(Main.factory.getGraphRepository().getAdjacentMatrix(), Main.factory.getGraphRepository().getVertexCount())) {
            System.out.println("Bipartite Graph");
            Main.factory.getGraphRepository().setGraphType(GraphType.BIPARTITE_GRAPH);
            return true;
        }

        if (GraphCheck.isCycleGraph(Main.factory.getGraphRepository().getAdjacentMatrix(), Main.factory.getGraphRepository().getVertexCount())) {
            System.out.println("Cycle Graph");
            Main.factory.getGraphRepository().setGraphType(GraphType.CYCLE_GRAPH);
            return true;
        }

        if (GraphCheck.isWheelGraph(Main.factory.getGraphRepository().getAdjacentMatrix(), Main.factory.getGraphRepository().getVertexCount())) {
            System.out.println("Wheel Graph");
            Main.factory.getGraphRepository().setGraphType(GraphType.WHEEL_GRAPH);
            return true;
        }

        if (GraphCheck.isTreeGraph(Main.factory.getGraphRepository().getAdjacentMatrix(), Main.factory.getGraphRepository().getVertexCount())) {
            System.out.println("Tree Graph");
            Main.factory.getGraphRepository().setGraphType(GraphType.TREE_GRAPH);
            return true;
        }

        return true;
    }
}
