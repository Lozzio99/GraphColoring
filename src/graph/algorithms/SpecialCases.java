package graph.algorithms;

import graph.Main;
import graph.utils.Util;

public class SpecialCases {
    public static void calculateNullGraph() {
        if (Main.factory.getGraphRepository().getVertexCount() > 0) {
            Main.factory.getGraphRepository().setChromaticNumber(1);
        } else {
            Main.factory.getGraphRepository().setChromaticNumber(0);
        }
    }

    public static void calculateCompleteGraph() {
        Main.factory.getGraphRepository().setChromaticNumber(Main.factory.getGraphRepository().getVertexCount());
    }

    public static void calculateStarGraph() {
        if (Main.factory.getGraphRepository().getVertexCount() == 1) {
            Main.factory.getGraphRepository().setChromaticNumber(1);
            return;
        }

        Main.factory.getGraphRepository().setChromaticNumber(2);
    }

    public static void calculateBipartiteGraph() {
        if (Main.factory.getGraphRepository().getVertexCount() == 0) {
            Main.factory.getGraphRepository().setChromaticNumber(0);
            return;
        }

        if (Main.factory.getGraphRepository().getEdgeCount() == 0) {
            Main.factory.getGraphRepository().setChromaticNumber(1);
            return;
        }

        Main.factory.getGraphRepository().setChromaticNumber(2);
    }

    public static void calculateCycleGraph() {
        int n = Main.factory.getGraphRepository().getVertexCount();

        if (Util.isEven(n)) {
            Main.factory.getGraphRepository().setChromaticNumber(2);
        } else {
            Main.factory.getGraphRepository().setChromaticNumber(3);
        }
    }

    public static void calculateWheelGraph() {
        int n = Main.factory.getGraphRepository().getVertexCount();

        if (Util.isEven(n)) {
            Main.factory.getGraphRepository().setChromaticNumber(4);
        } else {
            Main.factory.getGraphRepository().setChromaticNumber(3);
        }
    }

    public static void calculateTreeGraph() {
        int n = Main.factory.getGraphRepository().getVertexCount();

        if (n == 1) {
            Main.factory.getGraphRepository().setChromaticNumber(1);
        }

        Main.factory.getGraphRepository().setChromaticNumber(2);
    }
}
