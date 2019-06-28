package com.alar.knapsack;

public class CreateCommand extends KnapsackCommand {
    private int capacity;
    private int[] weights;
    private int[] values;

    public CreateCommand(int capacity, int[] weights, int[] values) {
        this.setCapacity(capacity);
        this.setWeights(weights);
        this.setValues(values);
    }

    public CreateCommand() {
    }

    public int getCapacity() {
        return capacity;
    }

    public int[] getWeights() {
        return weights;
    }

    public int[] getValues() {
        return values;
    }

    public static CreateCommand of(int capacity, int[] weights, int[] values) {
        return new CreateCommand(capacity, weights, values);
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public void setWeights(int[] weights) {
        this.weights = weights;
    }

    public void setValues(int[] values) {
        this.values = values;
    }
}
