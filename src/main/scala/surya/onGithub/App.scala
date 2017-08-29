package surya.onGithub

import akka.actor.ActorSystem
import akka.stream._
import com.spotify.docker.client.DefaultDockerClient
import org.mongodb.scala._
import surya.onGithub.actors.DockerContainerLauncher
import surya.onGithub.di.{DI, Services}

import scala.language.postfixOps



object Main {
  implicit val as = ActorSystem()
  implicit val ec = as.dispatcher
  val settings = ActorMaterializerSettings(as)
  implicit val mat = ActorMaterializer(settings)

  def main(args: Array[String]): Unit = {
    val dockerClient = DefaultDockerClient.builder.uri("http://localhost:2376").build
    val mongoClient: MongoClient = MongoClient()
    val mongoDB: MongoDatabase = mongoClient.getDatabase("meow2")

    val dependencies = Services(dockerClient,mongoDB)
    DI.instance.get(as).initialize(dependencies)
    val dockerLauncherActor = as.actorOf(DI.instance.get(as).props(classOf[DockerContainerLauncher]))
    dockerLauncherActor  ! "busybox"


  }

}
