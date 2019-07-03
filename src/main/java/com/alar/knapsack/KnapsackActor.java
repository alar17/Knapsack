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

/**
 * The Knapsack actor is responsible for solving the problem and sending the result back if requested.
 * It's extendted the Akka actors. Please read the Akka actor documentation for more information.
 * @author alar
 */
public class KnapsackActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);
    private Option<KnapsackResult.Result> result;
    private final UUID id;

    public KnapsackActor(UUID id) {
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
        result = Option.some(knapsackAlgorithm(cmd.getCapacity(), cmd.getWeights(), cmd.getValues()));
    }

    private KnapsackResult.Result knapsackAlgorithm(Integer c, int[] w, int[] v) {
        System.out.println("Solution:");
        return Solver.solve(c.intValue(), w, v);
    }
}
