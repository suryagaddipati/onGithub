package surya.onGithub

import akka.actor.ActorSystem
import akka.http.scaladsl.model.{ContentTypes, HttpEntity}
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.stream._
import akka.stream.scaladsl.Source
import com.spotify.docker.client.DefaultDockerClient
import org.mongodb.scala._
import surya.onGithub.actors.MainRunner
import surya.onGithub.actors.github.{BhutigAPI, HookShot}
import surya.onGithub.di.{DI, Services}


object WebServer  extends HttpApp with App {
  implicit val as = ActorSystem()
  implicit val ec = as.dispatcher
  val settings = ActorMaterializerSettings(as)
  implicit val mat = ActorMaterializer(settings)

  val config = new Config()
  val dockerClient = DefaultDockerClient.builder.uri("http://localhost:2376").build
  val mongoClient: MongoClient = MongoClient()
  val mongoDB: MongoDatabase = mongoClient.getDatabase("meow2")
  val buhtig = new BhutigAPI(config.githubToken,config.githubApiUrl)
  val githubClient = buhtig.syncClient


  val dependencies = Services(dockerClient,mongoDB,githubClient)
  DI.instance.get(as).initialize(dependencies)


  override protected def routes: Route =
    pathEndOrSingleSlash {
      complete("Server up and running")
    } ~
      path("github-webhook") {
        post {

          (formField('payload) &  headerValueByName("X-GitHub-Delivery") & headerValueByName("X-GitHub-Event") ) { (payload,hookId,event) =>
            val mainRunner = as.actorOf(DI.instance.get(as).props(classOf[MainRunner]))
            mainRunner  ! HookShot(hookId,event,payload)
            complete("received")
          }
        }
      }



  startServer(config.httpHost, config.httpPort)
}

