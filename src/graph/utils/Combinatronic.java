package graph.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.LongStream;

public class Combinatronic {
    /**
     * nCr is calculated using (n!) / (r! * (n-r)!)
     * The number of elements may grow exponentially (worst case scenario),
     * which might make it very difficult (to impossible) to calculate using the default mathematical way.
     *
     * Instead an Iterative algorithm is used, which generated the combination of the current iteration, up until all combinations are calculated.
     * Source: Generate Combinations in Java | Baeldung. (2020, March 25). Retrieved from https://www.baeldung.com/java-combinations-algorithm
     * @param n - objects
     * @param r - samples
     * @return - the value of nCr
     */
    public static int nCr(int n, int r) {
        List<int[]> combinations = new ArrayList<>();
        int[] combination = new int[r];

        for (int i = 0; i < r; i++) {
            combination[i] = i;
        }

        while (combination[r -1] < n) {
            combinations.add(combination.clone());

            int t = r - 1;
            while (t != 0 && combination[t] == n - r + t) {
                t--;
            }

            combination[t]++;

            for (int i = t + 1; i < r; i++) {
                combination[i] = combination[i - 1] + 1;
            }
        }

        return combinations.size();
    }
}
