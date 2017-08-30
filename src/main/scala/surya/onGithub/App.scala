package surya.onGithub

import akka.actor.ActorSystem
import akka.stream._
import com.google.common.io.BaseEncoding
import com.spotify.docker.client.DefaultDockerClient
import dispatch.{Http, Req, host}
import net.caoticode.buhtig.{AsyncClient, Buhtig, SyncClient}
import net.caoticode.buhtig.Converters.JSON
import org.mongodb.scala._
import surya.onGithub.actors.github.{BhutigAPI, HookShot}
import surya.onGithub.actors.{DockerContainerLauncher, MainRunner}
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

    val dependencies = Services(dockerClient,mongoDB)
    DI.instance.get(as).initialize(dependencies)
    val mainRunner = as.actorOf(DI.instance.get(as).props(classOf[MainRunner]))
    //    mainRunner  ! HookShot("id","push","payload-meow")
    val buhtig = new BhutigAPI("","api.github.com")
    val client = buhtig.syncClient
    import net.caoticode.buhtig.Converters._
    val repo = client.repos.mdread.buhtig.contents.LICENSE.get[JSON]
    val contents = (repo \\ "content").values.asInstanceOf[String]
    println(contents)
    println(new String(BaseEncoding.base64().decode(contents.trim.replaceAll("\\n",""))))
    buhtig.close()

  }


}
