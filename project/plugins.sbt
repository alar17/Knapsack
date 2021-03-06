addSbtPlugin("com.typesafe.sbt" % "sbt-native-packager" % "1.3.24")
addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "5.2.2")
addSbtPlugin("io.spray" % "sbt-revolver" % "0.9.1")
addSbtPlugin("com.typesafe.sbt" % "sbt-git" % "0.9.3")
addSbtPlugin("com.github.gseitz" % "sbt-release" % "1.0.10")
addSbtPlugin("com.lightbend.sbt" % "sbt-javaagent" % "0.1.4")

libraryDependencies += "com.github.os72" % "protoc-jar" % "3.5.1.1"
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.3")
