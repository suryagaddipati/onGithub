name := "onGithub"

version := "0.1.0 "

scalaVersion := "2.11.5"

val akkaVersion = "2.3.7"
lazy val akkaHttpVersion = "10.0.7"

libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % akkaVersion,
  "com.typesafe.akka" %% "akka-http"   % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-cluster" % akkaVersion,
  "com.typesafe.akka" %% "akka-slf4j" % akkaVersion,
  "com.typesafe.akka" %% "akka-agent" % akkaVersion,
  "com.spotify" % "docker-client" % "8.8.0",
  "net.databinder.dispatch" %% "dispatch-core" % "0.11.2",
  "net.databinder.dispatch" %% "dispatch-json4s-native" % "0.11.2",
  "org.mongodb.scala" %% "mongo-scala-driver" % "2.1.0",
  "net.caoticode.buhtig" %% "buhtig" % "0.3.1"
)

scalacOptions ++= Seq("-feature")
