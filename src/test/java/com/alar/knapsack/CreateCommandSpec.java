package com.alar.knapsack;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forgerock.cuppa.Cuppa.describe;
import static org.forgerock.cuppa.Cuppa.it;
import static org.forgerock.cuppa.Cuppa.when;

import org.forgerock.cuppa.junit.CuppaRunner;
import org.junit.runner.RunWith;

@RunWith(CuppaRunner.class)
public class CreateCommandSpec {{
    describe("CreateCommand", () -> {
        CreateCommand command = new CreateCommand();
        when("setting the parameters", () -> {
            int[] weights = new int[3];
            int[] values = new int[3];
            weights[0] = 10;
            weights[1] = 20;
            weights[2] = 30;
            values[0] = 15;
            values[1] = 25;
            values[2] = 35;
            command.setCapacity(12);
            command.setValues(values);
            command.setWeights(weights);
            it("should be able to get the paramters as they have been set", () -> {
                assertThat(command.getCapacity()).isEqualTo(12);
                assertThat(command.getValues().length).isEqualTo(3);
                assertThat(command.getValues()[0]).isEqualTo(15);
                assertThat(command.getValues()[1]).isEqualTo(25);
                assertThat(command.getValues()[2]).isEqualTo(35);
                assertThat(command.getWeights().length).isEqualTo(3);
                assertThat(command.getWeights()[0]).isEqualTo(10);
                assertThat(command.getWeights()[1]).isEqualTo(20);
                assertThat(command.getWeights()[2]).isEqualTo(30);
            });
        });
    });
}}
