package com.alar.knapsack;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import akka.actor.AbstractActor;
import akka.actor.AbstractActor.Receive;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Terminated;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import io.vavr.collection.List;
import io.vavr.control.Option;

import com.alar.knapsack.KnapsackResult.ResponseType;
import com.alar.knapsack.KnapsackResult.Result;

public class KnapsackActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    //private final ActorRef actorRef;
    private ActorRef lastSender;
    private Option<KnapsackResult.Result> result;
    private final UUID id;
//    public KnapsackActor(ActorSystem system) {
//      //getContext().watch(child); // <-- this is the only call needed for registration
//      //lastSender = system.deadLetters();
//    }

//    public KnapsackActor() {
//        //this.actorRef = actorRef; //getContext().actorOf(Props.empty(), "target");
//    }

    public KnapsackActor(UUID id) {
        //this(getContext().actorOf(Props.empty(), id.toString()));
        this.id = id;
    }

    @Override
    public Receive createReceive() {
      return receiveBuilder()
          .match(
              ReadCommand.class,
              s -> {
                  log.info("Received Read message: {}", s);
                  if (result.isDefined()) {
                      getSender().tell(KnapsackResult.completed(id, result.get()), getSelf());
                  } else {
                      getSender().tell(KnapsackResult.processing(id), getSelf());
                  }
              })
          .match(
                  CreateCommand.class,
                  s -> {
                      log.info("Received Create message: {}", s.getValues());

                      handleCreate(s);
                      getSender().tell(KnapsackResult.accepted(id), getSelf());
                  })
          .matchAny(o -> log.info("received unknown message"))
          .build();
    }

    private KnapsackResult handleRead() {
        return result.isEmpty() ? KnapsackResult.processing(id) : KnapsackResult.completed(id, result.get());
    }

    private void handleCreate(CreateCommand cmd) {
        System.out.println("Handling Create");
        result = Option.some(knapsackAlgorithm(cmd.getCapacity(), cmd.getWeights(), cmd.getValues()));
//        CompletableFuture.runAsync(() -> {
//            System.out.println("Handling Create Async!");
//            result = Option.some(knapsackAlgorithm(cmd.getCapacity(), cmd.getWeights(), cmd.getValues()));
//        });
    }

    private KnapsackResult.Result knapsackAlgorithm(Integer c, int[] w, int[] v) {
        List<Integer> weights = List.of(Integer.valueOf(12));
        List<Integer> values = List.of(Integer.valueOf(12));
        System.out.println("Solution:");
        // TODO:Insert the algorithm here
        int r = Solver.solve(c.intValue(), w, v);
        System.out.println("Result: " + r);

        return KnapsackResult.of(weights, values);
    }
}
