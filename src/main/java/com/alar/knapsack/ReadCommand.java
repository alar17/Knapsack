package com.alar.knapsack;

public class ReadCommand extends KnapsackCommand {

    public static ReadCommand getInstance() {
        return new ReadCommand();
    }

    ReadCommand() {
    }

    public static ReadCommand of() {
        return new ReadCommand();
    }
}
