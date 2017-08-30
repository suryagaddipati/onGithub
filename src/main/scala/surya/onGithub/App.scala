package surya.onGithub

import akka.actor.ActorSystem
import akka.stream._
import com.spotify.docker.client.DefaultDockerClient
import org.mongodb.scala._
import surya.onGithub.actors.MainRunner
import surya.onGithub.actors.github.{BhutigAPI, HookShot}
import surya.onGithub.di.{DI, Services}


object Main {
  implicit val as = ActorSystem()
  implicit val ec = as.dispatcher
  val settings = ActorMaterializerSettings(as)
  implicit val mat = ActorMaterializer(settings)

  def main(args: Array[String]): Unit = {
    val dockerClient = DefaultDockerClient.builder.uri("http://localhost:2376").build
    val mongoClient: MongoClient = MongoClient()
    val mongoDB: MongoDatabase = mongoClient.getDatabase("meow2")
    val config = new Config()
    val buhtig = new BhutigAPI(config.githubToken,config.githubApiUrl)
    val githubClient = buhtig.syncClient


    val dependencies = Services(dockerClient,mongoDB,githubClient)
    DI.instance.get(as).initialize(dependencies)
    val mainRunner = as.actorOf(DI.instance.get(as).props(classOf[MainRunner]))
    mainRunner  ! HookShot("id","push","payload-meow")
    //    buhtig.close()

  }
}

