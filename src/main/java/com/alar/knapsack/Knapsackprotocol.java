package com.alar.knapsack;

import java.util.UUID;
import java.util.concurrent.CompletionStage;

import akka.actor.AbstractActor;
import akka.actor.ActorPath;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.AbstractActor.Receive;
import akka.pattern.PatternsCS;
import io.vavr.collection.List;

public class Knapsackprotocol {

    ActorSystem system;
    ActorRef knapsack;
    public Knapsackprotocol(ActorSystem system) {
        this.system = system;
    }

    public CompletionStage<KnapsackResult> read(UUID id) {
        CompletionStage<KnapsackResult> read = ask(id, (KnapsackCommand) ReadCommand.getInstance());
        read.thenApply(s -> {
            System.out.println(" *** Final Result ****" + s);
            System.out.println("Type:" + s.getResponseType());
            System.out.println("ID:" + s.getId());
            if (s.getResult().isDefined()) {
                System.out.println("Result: " + s.getResult().get().getValues() + "---" + s.getResult().get().getWeights());
            }
            return s;
        });
        return read;
    }

    public CompletionStage<KnapsackResult> createKnapsackActor(CreateCommand cmd) {
        // creates an id with the new UUID. Pass the paramters
        UUID id = UUID.randomUUID();

        knapsack = system.actorOf(Props.create(KnapsackActor.class, id), id.toString());//new KnapsackActor(getContext().actorOf(Props.empty(), id.toString()));
        System.out.println("ActorRef: " + knapsack);
        System.out.println("Path: " + knapsack.path());
        System.out.println("Terminated: " + knapsack.isTerminated());
        CompletionStage<KnapsackResult> result = ask(id, cmd);
        System.out.println("Provisional Result:" + result);
        result.thenApply(s -> {
            System.out.println(" *** Final Result ****" + s);
            System.out.println("Type:" + s.getResponseType());
            System.out.println("ID:" + s.getId());
            System.out.println("Result:" + s.getResult());
            return s;
        });
        return result;
    }

    protected CompletionStage<KnapsackResult> ask(UUID id, KnapsackCommand cmd) {
        System.out.println("received ask for:" + id + " --- command: " + cmd);
        ActorSelection selection = selection(id);

        return PatternsCS.ask(selection, cmd, 1000)
            .thenApply(resp -> (KnapsackResult) resp);
    }

    public ActorSelection selection(UUID id) {
        // TODO: FIND THE ACTUAL ONE BASED ON ID
        return system.actorSelection(knapsack.path());
    }
}
