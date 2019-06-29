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

/**
 * The protocol class which is responsible for craeting new actors, route commands to the recipient actor,
 * and send back the response to the directive layer. This class can be a sharding protocol, if we need to serve many simultaneous requests.
 * @author alar
 *
 */
public class KnapsackProtocol {
    ActorSystem system;
    ActorRef knapsack;

    public KnapsackProtocol(ActorSystem system) {
        this.system = system;
    }

    public CompletionStage<KnapsackResult> read(UUID id) {
        CompletionStage<KnapsackResult> read = ask(id, (KnapsackCommand) ReadCommand.getInstance());
        read.thenApply(s -> {
            if (s.getResult().isDefined()) {
                System.out.println("Result: " + s.getResult().get().getValues() + "---" + s.getResult().get().getWeights());
            }
            return s;
        });
        return read;
    }

    /**
     * Create a new knapsackActor which is responsible for solving an specific knapsack problem and sending the result later
     */
    public CompletionStage<KnapsackResult> createKnapsackActor(CreateCommand cmd) {
        // creates an id with the new UUID. Pass the paramters
        UUID id = UUID.randomUUID();

        // Creating the actor
        knapsack = system.actorOf(Props.create(KnapsackActor.class, id), id.toString());//new KnapsackActor(getContext().actorOf(Props.empty(), id.toString()));

        // The provisional result of creating the problem. Asking the actor to solve the Knapsack problem Asynchronously
        return ask(id, cmd);
    }

    /**
     * Sending the ask command to the actor with id in the path
     */
    private CompletionStage<KnapsackResult> ask(UUID id, KnapsackCommand cmd) {
        System.out.println("received ask for:" + id + " --- command: " + cmd);
        ActorSelection selection = selection(id);

        return PatternsCS.ask(selection, cmd, 2000)
            .thenApply(resp -> (KnapsackResult) resp);
    }

    /**
     * Finding the target Actor
     */
    private ActorSelection selection(UUID id) {
        return system.actorSelection(ActorPath.fromString("akka://default/user/" + id.toString()));
    }
}
