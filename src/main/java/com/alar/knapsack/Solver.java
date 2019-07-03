package com.alar.knapsack;

import io.vavr.collection.List;

// from https://medium.com/@ssaurel/solving-the-knapsack-problem-in-java-c985c71a7e64
public class Solver {
 // we write the solve algorithm
    static public KnapsackResult.Result solve(int capacity, int weight[], int value[]) {
      int NB_ITEMS = value.length;
      // we use a matrix to store the max value at each n-th item
      int[][] matrix = new int[NB_ITEMS + 1][capacity + 1];

      // first line is initialized to 0
      for (int i = 0; i <= capacity; i++)
        matrix[0][i] = 0;

      // we iterate on items
      for (int i = 1; i <= NB_ITEMS; i++) {
        // we iterate on each capacity
        for (int j = 0; j <= capacity; j++) {
          if (weight[i - 1] > j)
            matrix[i][j] = matrix[i-1][j];
          else
            // we maximize value at this rank in the matrix
            matrix[i][j] = Math.max(matrix[i-1][j], matrix[i-1][j - weight[i-1]]
                    + value[i-1]);
        }
      }

      int res = matrix[NB_ITEMS][capacity];
      int w = capacity;
      List<Integer> values = List.empty();
      List<Integer> weights = List.empty();

      for (int i = NB_ITEMS; i > 0  &&  res > 0; i--) {
        if (res != matrix[i-1][w]) {
            values = values.append(value[i-1]);
            weights = weights.append(weight[i-1]);
          // we remove items value and weight
          res -= value[i-1];
          w -= weight[i-1];
        }
      }

      return KnapsackResult.of(weights, values);
    }
}
