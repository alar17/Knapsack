package com.alar.knapsack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forgerock.cuppa.Cuppa.describe;
import static org.forgerock.cuppa.Cuppa.it;
import static org.forgerock.cuppa.Cuppa.when;
import static org.junit.Assert.assertTrue;

import java.util.UUID;

import org.forgerock.cuppa.junit.CuppaRunner;
import org.junit.runner.RunWith;

import com.alar.knapsack.KnapsackResult.ResponseType;

import io.vavr.collection.List;

@RunWith(CuppaRunner.class)
public class KnapsackResultSpec {{
    describe("KnapsackResult", () -> {
        when("Knapsack problem is just created and accepted", () -> {
            UUID id = UUID.fromString("1E09EA75-8DB6-4742-AEAA-5236B1C5A582");
            final KnapsackResult knapsackResult = KnapsackResult.accepted(id);
            it("should have accepted type including the ID of the problem", () -> {
                assertThat(knapsackResult.getResponseType()).isEqualTo(ResponseType.Accepted);
                assertThat(knapsackResult.getId()).isEqualTo(id);
            });

            it("result should not be defined yet", () -> {
                assertTrue(knapsackResult.getResult().isEmpty());
            });
        });

        when("Knapsack problem is still processing", () -> {
            UUID id = UUID.fromString("F4C88AAA-3B7F-4DEB-B02C-0FA53810FEAA");
            final KnapsackResult knapsackResult = KnapsackResult.processing(id);
            it("should have processing type including the ID of the problem", () -> {
                assertThat(knapsackResult.getResponseType()).isEqualTo(ResponseType.Processing);
                assertThat(knapsackResult.getId()).isEqualTo(id);
            });

            it("result should not be defined yet", () -> {
                assertTrue(knapsackResult.getResult().isEmpty());
            });
        });

        when("Knapsack problem is just created and accepted", () -> {
            UUID id = UUID.fromString("F76EA89C-CB9E-48A2-A862-B93DA8188D08");
            KnapsackResult.Result result = new KnapsackResult.Result(List.of(12), List.of(10));
            final KnapsackResult knapsackResult = KnapsackResult.completed(id, result);
            it("should have completed type including the ID of the problem", () -> {
                assertThat(knapsackResult.getResponseType()).isEqualTo(ResponseType.Completed);
                assertThat(knapsackResult.getId()).isEqualTo(id);
            });

            it("result should be ready", () -> {
                assertTrue(knapsackResult.getResult().isDefined());
                assertThat(knapsackResult.getResult().get().getValues().get(0)).isEqualTo(10);
                assertThat(knapsackResult.getResult().get().getWeights().get(0)).isEqualTo(12);
            });
        });
    });
}}
