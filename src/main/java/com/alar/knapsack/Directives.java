package com.alar.knapsack;

import static akka.http.javadsl.server.PathMatchers.segment;
import static akka.http.javadsl.server.PathMatchers.uuidSegment;

import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;

import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.Route;
import akka.stream.ActorMaterializer;
import akka.stream.javadsl.Flow;

/**
 * Directives and Routes in order to handle http requests and responses.
 * Extending Akka Directives
 * For more information, please read Akka directives documentation:
 * https://doc.akka.io/docs/akka-http/current/routing-dsl/directives/index.html
 * @author alar
 */
public class Directives extends AllDirectives {
    private final KnapsackProtocol protocol;
    private final ActorSystem system;

    public Directives() {
        system = ActorSystem.create();
        protocol = new KnapsackProtocol(system);
        runServer();
    }
    /**
     * Returns a route matching resources that should be accessible over HTTP.
     */
    public Route http() {

        /**
         * Route for handling post request for creating a new knapsack problem
         */
        Route postKnapsack = post(() ->
            entity(Jackson.unmarshaller(CreateCommand.class), cmd -> {
                CompletionStage<KnapsackResult> result = protocol.createKnapsackActor(cmd);
                KnapsackResult completedResponse;
                try {
                    completedResponse = result.toCompletableFuture().get();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Exception = " + e);
                    return reject();
                }
                return complete("Received POST request for order: " + completedResponse.getId());
            })
        );

        /**
        * Route for handling get request for getting the solution of the problem which has been created before
        */
        Route getKnapsack = get(() -> {
            System.out.println("GET RECEIVED");
            return path(segment("knapsack").slash(uuidSegment()), id -> {
                System.out.println("UUID:" + id);
                CompletionStage<KnapsackResult> result = protocol.read(id);
                KnapsackResult completedResponse;
                try {
                    completedResponse = result.toCompletableFuture().get();
                } catch (InterruptedException | ExecutionException e) {
                    System.out.println("Exception = " + e);
                    return reject();
                }
                return complete("Received GET request for order " + id + "\n Result" + completedResponse);
            });
        });
        return postKnapsack.orElse(getKnapsack);
    }

    /**
     * Run the server, start actor system and bind the port 8080 in order to listen to requests
     */
    public void runServer() {
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Http http = Http.get(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = http().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("0.0.0.0", 8080), materializer);
    }
}
