val akkaV = "2.5.16"
val akkaHttpV = "10.1.4"
val reaktiveV = "0.12.1"


libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-jackson" % akkaHttpV,
    "com.typesafe.akka" %% "akka-stream" % akkaV,
    "com.typesafe.akka" %% "akka-cluster" % akkaV,
    "com.typesafe.akka" %% "akka-remote" % akkaV,
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-persistence" % akkaV,
    "com.typesafe.akka" %% "akka-cluster-sharding" % akkaV,
    "com.typesafe.akka" %% "akka-distributed-data" % akkaV,
    "com.typesafe.akka" %% "akka-cluster-tools" % akkaV,
    "com.typesafe.akka" %% "akka-persistence-query" % akkaV,
    "com.github.ben-manes.caffeine" % "caffeine" % "2.5.5", // caching library
    "io.kamon" %% "kamon-core" % "1.1.3",
    "io.vavr" % "vavr" % "0.9.0",
    "com.ibm.icu" % "icu4j" % "63.1",
    "junit" % "junit" % "4.11" % "test",
    "org.assertj" % "assertj-core" % "3.2.0" % "test",
    "org.mockito" % "mockito-core" % "2.2.27" % "test",
    "info.solidsoft.mockito" % "mockito-java8" % "2.0.0" % "test",
    "com.novocode" % "junit-interface" % "0.11" % "test",
    "org.forgerock.cuppa" % "cuppa" % "1.3.1" % "test",
    "org.forgerock.cuppa" % "cuppa-junit" % "1.3.1" % "test",
    "org.quicktheories" % "quicktheories" % "0.13" % "test",
    "org.xmlunit" % "xmlunit-core" % "2.6.2" % "test",
    "com.typesafe.akka" %% "akka-testkit" % akkaV % "test",
    "io.github.oliviercailloux" % "google-or-tools" % "6.7.2",
    "com.typesafe.akka" %% "akka-http-jackson" % "akkaV"
  );
