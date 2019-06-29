package com.alar.knapsack;

/**
 * A type of KnapsackCommand that can be send to Knapsack actor in order to get the result.
 * @author alar
 *
 */
public class ReadCommand extends KnapsackCommand {
    public static ReadCommand getInstance() {
        return new ReadCommand();
    }

    private ReadCommand() {
    }
}
