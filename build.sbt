name := "onGithub"

version := "0.1.0 "

scalaVersion := "2.11.5"

val akkaVersion = "2.3.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-agent" % akkaVersion,
  "com.spotify" % "docker-client" % "8.8.0",
  "com.typesafe.akka" %% "akka-stream" % "2.5.3",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.11.2",
  "com.google.protobuf" % "protobuf-java" % "2.5.0",
  "com.fasterxml.jackson.module" %% "jackson-module-scala" % "2.4.2",
  "com.typesafe.akka" %% "akka-stream-kafka" % "0.16",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0",
  "org.scalaz" %% "scalaz-core" % "7.1.0",
  "com.typesafe.akka" %% "akka-actor" % "2.5.4",
  "net.caoticode.buhtig" %% "buhtig" % "0.3.1"
)

scalacOptions ++= Seq("-feature")
