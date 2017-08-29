package surya.onGithub.actors

import akka.actor.Actor
import com.spotify.docker.client.messages.ContainerConfig
import surya.onGithub.di.{DI, Services}

import scala.language.postfixOps

class DockerContainerLauncher(services: Services) extends Actor{
  override def receive: Receive = {
    case image:String => {

      val config = ContainerConfig.builder.image(image).cmd("sh","-c","while :; do echo `date`; sleep 3; done").build
      val creation = services.dockerClient.createContainer(config)
      val id = creation.id
      services.dockerClient.startContainer(id)
      val dockerContainerLogSaver = context.system.actorOf(DI.instance.get(context.system).props(classOf[DockerContainerLogSaver]))
      dockerContainerLogSaver  ! id

    }
  }
}
