package com.alar.knapsack;

import static akka.http.javadsl.server.Directives.complete;
import static akka.http.javadsl.server.Directives.getFromResource;
import static akka.http.javadsl.server.Directives.handleExceptions;
import static akka.http.javadsl.server.Directives.pathEndOrSingleSlash;
import static akka.http.javadsl.server.Directives.redirectToTrailingSlashIfMissing;
import static akka.http.javadsl.server.Directives.route;
import static akka.http.javadsl.server.Directives.redirect;
import static akka.http.javadsl.server.Directives.complete;
import static akka.http.javadsl.server.Directives.reject;
import static akka.http.javadsl.server.Directives.get;
import static akka.http.javadsl.server.Directives.path;
import static akka.http.javadsl.server.Directives.put;
import static akka.http.javadsl.server.Directives.pathSingleSlash;
import static akka.http.javadsl.unmarshalling.StringUnmarshallers.INTEGER;

import akka.http.javadsl.marshallers.jackson.Jackson;
import akka.Done;
import akka.NotUsed;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.ActorRef;
import akka.cluster.Cluster;
import java.util.UUID;
import akka.cluster.pubsub.DistributedPubSub;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.ConnectHttpsImpl;
import akka.http.javadsl.ConnectionContext;
import akka.http.javadsl.Http;
import akka.http.javadsl.HttpsConnectionContext;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.UseHttp2;
import akka.http.javadsl.marshalling.Marshaller;
import akka.persistence.query.PersistenceQuery;
import akka.stream.ActorMaterializer;
import akka.stream.TLSClientAuth;
import akka.stream.javadsl.Flow;
import akka.stream.javadsl.Sink;
import akka.stream.javadsl.Source;
import io.vavr.collection.HashMap;
import io.vavr.collection.List;
import static akka.http.javadsl.server.PathMatchers.segment;
import static akka.http.javadsl.server.PathMatchers.uuidSegment;

import java.util.NoSuchElementException;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.regex.Pattern;

import javax.xml.stream.XMLStreamException;

import org.xml.sax.SAXParseException;

import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import akka.http.javadsl.model.ContentTypes;
import akka.http.javadsl.model.HttpEntities;
import akka.http.javadsl.model.HttpEntity;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.MediaType;
import akka.http.javadsl.model.RequestEntity;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.model.Uri;
import akka.http.javadsl.server.AllDirectives;
import akka.http.javadsl.server.ExceptionHandler;
import akka.http.javadsl.server.PathMatcher1;
import akka.http.javadsl.server.PathMatchers;
import akka.http.javadsl.server.Route;
import akka.http.javadsl.unmarshalling.Unmarshaller;
import akka.http.scaladsl.model.EntityStreamSizeException;
import akka.japi.pf.PFBuilder;
import akka.stream.BufferOverflowException;
import akka.stream.StreamTcpException;

public class Directives extends AllDirectives {
/**
### REST API

The user must be able to interact with the solution via the REST API specified here.
See the examples section below for their usage with `curl`.

* POST `/knapsack`
content: `application/json` with JSON knapsack problem specification
output: `json` with JSON knapsack object

* GET `/knapsack/<id>`
output: `json` with JSON knapsack object
 */
 // Create Protocol Here
    private final Knapsackprotocol protocol;
    private final ActorSystem system;

    public Directives() {
        system = ActorSystem.create();
        protocol = new Knapsackprotocol(system);
        runServer();
    }
    /**
     * Returns a route matching resources that should be accessible over HTTP.
     */
    public Route http() {
        Route postKnapsack = post(() ->
            entity(Jackson.unmarshaller(CreateCommand.class), s -> {
                CompletionStage<KnapsackResult> result = protocol.createKnapsackActor(s);
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

          Route getKnapsack = get(() -> {
              System.out.println("GET RECEIVED");
              return path(segment("knapsack").slash(uuidSegment()), id -> {
                  System.out.println("UUID:" + id);

                  protocol.read(id);
                  return complete("Received GET request for order " + id);
              });
          });
        return postKnapsack.orElse(getKnapsack);
    }

    public void runServer() {
        final ActorMaterializer materializer = ActorMaterializer.create(system);

        final Http http = Http.get(system);
        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = http().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow, ConnectHttp.toHost("localhost", 8080), materializer);
    }

    public static ObjectMapper mapper = new ObjectMapper().enable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
    Unmarshaller<HttpEntity,CreateCommand> fromJSON =  Jackson.unmarshaller(mapper, CreateCommand.class);
}
