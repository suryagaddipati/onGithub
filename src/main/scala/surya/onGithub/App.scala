package surya.onGithub

import java.io.{PipedInputStream, PipedOutputStream}
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

import akka.actor.ActorSystem
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.server.{HttpApp, Route}
import akka.stream._
import akka.stream.scaladsl.StreamConverters
import com.spotify.docker.client.DefaultDockerClient
import org.mongodb.scala._
import org.mongodb.scala.gridfs.GridFSBucket
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

  import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._
  import org.mongodb.scala.gridfs.helpers.AsyncStreamHelper._




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
      }~
      path("streaming-text") {
        get {
          val dstByteBuffer: ByteBuffer = ByteBuffer.allocate(1024*1024)

          val gridFSBucket = GridFSBucket(mongoDB)

          val mongoStream = new PipedOutputStream()
          val mongoStream1 = new PipedInputStream(mongoStream)
          gridFSBucket.downloadToStream("95b53f763081cd18e90ef39dbb742507fdc65e0eacb394551ba25ed2067fab10",toAsyncOutputStream(mongoStream)).head()


          val mongoStreamSource = StreamConverters.fromInputStream(() => mongoStream1,5000)

          complete {
            mongoStreamSource
              .map(s => ServerSentEvent(s.decodeString(StandardCharsets.UTF_8)))
//              .keepAlive(1.second, () => ServerSentEvent.heartbeat)
          }

        }
      }



  startServer(config.httpHost, config.httpPort)
}

