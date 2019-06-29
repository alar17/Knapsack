package com.alar.knapsack;
/**
 * The solving algorithm of Knapsack problem which has been taken from the following resource:
 * A Dynamic Programming based solution for 0-1 Knapsack problem
 * https://www.geeksforgeeks.org/java-program-for-dynamic-programming-set-10-0-1-knapsack-problem/
 */
class Solver {

    // A utility function that returns maximum of two integers
    static int max(int a, int b) { return (a > b) ? a : b; }

    // Returns the maximum value that can be put in a knapsack of capacity W
    static int solve(int W, int wt[], int val[]) {
    //static int solve() {
        int n = val.length;
        int i, w;
        int K[][] = new int[n + 1][W + 1];

        // Build table K[][] in bottom up manner
        for (i = 0; i<= n; i++) {
            for (w = 0; w<= W; w++) {
                if (i == 0 || w == 0)
                    K[i][w] = 0;
                else if (wt[i - 1]<= w)
                    K[i][w] = max(val[i - 1] + K[i - 1][w - wt[i - 1]], K[i - 1][w]);
                else
                    K[i][w] = K[i - 1][w];
            }
        }

        return K[n][W];
    }
}
/*This code is contributed by Rajat Mishra */