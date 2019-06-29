package com.alar.knapsack;

import java.util.UUID;

import io.vavr.collection.List;
import io.vavr.control.Option;

/**
 * A wrapper for result of the knapsack problem.
 * @author alar
 *
 */
public class KnapsackResult {

    /**
     * Accepted: The actor started to solve the Knapsack problem
     * Processing: The result is not ready yet
     * Completed: The solution is ready
     * @author alar
     */
    public enum ResponseType {
        Accepted, Processing, Completed
    }

    private final ResponseType responseType;
    private Option<Result> result;
    private final UUID id;

    private KnapsackResult(UUID id, ResponseType responseType, Option<Result> result) {
        this.id = id;
        this.responseType = responseType;
        this.setResult(result);
    }

    public static KnapsackResult accepted(UUID id) {
        return new KnapsackResult(id, ResponseType.Accepted, Option.none());
    }

    public static KnapsackResult processing(UUID id) {
        return new KnapsackResult(id, ResponseType.Processing, Option.none());
    }

    public static KnapsackResult completed(UUID id, Result result) {
        return new KnapsackResult(id, ResponseType.Completed, Option.some(result));
    }

    public static Result of(List<Integer> weights, List<Integer> values) {
        return new Result(weights, values);
    }

    public ResponseType getResponseType() {
        return responseType;
    }

    public Option<Result> getResult() {
        return result;
    }

    public void setResult(Option<Result> result) {
        this.result = result;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return "ResponseType" + responseType + "\n Result:" + result;

    }

    /**
     * The result of knapsack problem including weights and values of the selected items.
     * @author alar
     *
     */
    public static class Result {
        private final List<Integer> weights;
        private final List<Integer> values;

        Result(List<Integer> weights, List<Integer> values) {
            this.weights = weights;
            this.values = values;
        }

        public List<Integer> getWeights() {
            return weights;
        }

        public List<Integer> getValues() {
            return values;
        }

        @Override
        public String toString() {
            return "Weights: " + weights + " - Values: " + values;
        }
    }
}
