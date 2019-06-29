# Knapsack
Knapsack using Akka

# Artichecture
This repository contains a PoC solution for knapsack problem, using the actor model architecture. The main focus of the solution is on the actor architecture [https://doc.akka.io/docs/akka/current/guide/tutorial_1.html] as opposed to knapsack algorithm. The algorith in the Solver class can be replaced easily by any other solutions.

The main two reason behind choosing actor architecture are scalability and concurrecy. 
Using an specific actor for solving a knapsack problem, allows us to easily receive requests concurrently from several users, without being concerned about handling it. Moreover, since actors communicate via sending and receiving messages, we can easily handle requests asynchronously. 

For more information and details about the architecture please read the following resources:

Akka actors [https://doc.akka.io/docs/akka/current/actors.html]
Akka Directives [https://doc.akka.io/docs/akka-http/current/routing-dsl/directives/index.html]
Akka Unmarshaller [https://doc.akka.io/docs/akka-http/current/common/unmarshalling.html]
AKka HTTP [https://doc.akka.io/docs/akka-http/current/index.html]

# Scalability 
By using a protocol, we can shard actors in different servers of the cluster. By increasing number of servers and cluster we can easily scale up to be able to respond to 10, 100, 1000 or 10,000 knapsack problems simultaneously.

# Run the server using sbt
In order to run the project using sbt, execute the following commands from the root folder of the project:

* sbt compile
* sbt run

Now that the server is running, you can reach to it via port 8080 using curl. In order to send a new Knapsack problem, please use the following command:

```
curl -i  -X POST -H 'Content-type: application/json' http://localhost:8080/knapsack/    -d '{"capacity": 60, "weights": [10, 20, 33], "values": [10, 3, 30]}'
```

The response of the server would be like:
```
HTTP/1.1 200 OK
Server: akka-http/10.1.4
Date: Sat, 29 Jun 2019 10:47:22 GMT
Content-Type: text/plain; charset=UTF-8
Content-Length: 69

Received POST request for order: 76dcd13d-ba38-4854-9892-fa88fb68bd0f
```

The response includes a UUID, which is the identifier of the requested problem. The server will solve it and prepare the result. You can ask for the result using the same UUID:

```
curl -i -X GET  http://localhost:8080/knapsack/76dcd13d-ba38-4854-9892-fa88fb68bd0f
```

If everything goes well, the server will respond with 200 OK, including the maximum possible value of the problem (in this case, 40)
```
HTTP/1.1 200 OK
Server: akka-http/10.1.4
Date: Sat, 29 Jun 2019 10:49:25 GMT
Content-Type: text/plain; charset=UTF-8
Content-Length: 147

Received GET request for order 76dcd13d-ba38-4854-9892-fa88fb68bd0f
 ResultResponseTypeCompleted
 Result:Some(40)
``` 

# Building docker image and publishing it
In order to build a docker image using sbt-native-packager[https://github.com/sbt/sbt-native-packager] execute the following commands:
* sbt stage
* sbt docker:publishLocal
* docker run -p 8080:8080 knapsack:0.1.0-SNAPSHOT

# Future work
This project has been made in a timeframe of 10 hours. The following items can be done in order to improve this PoC to a production quality code:
* Add command validation phase to the actors.
* Add exception handling in order to return a nice messsage to the user in case of failure
* Adding unit tests for Akka classes using akka test suite
* Adding integration tests, in order to protect the main functioanlity of the server
* Create a docker-compose file in order to include all the dependencies




