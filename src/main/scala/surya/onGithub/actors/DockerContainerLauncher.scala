package surya.onGithub.actors

import com.spotify.docker.client.messages.ContainerConfig
import surya.onGithub.di.{DI, Services}

import scala.language.postfixOps

class DockerContainerLauncher(services: Services) extends BaseActor{
  override def receive: Receive = {
    case image:String => {

      val config = ContainerConfig.builder.image(image).cmd("sh","-c","while :; do echo `date`; sleep 3; done").build
      val creation = services.dockerClient.createContainer(config)
      val id = creation.id
      services.dockerClient.startContainer(id)
      val dockerContainerLogSaver = getActor(classOf[DockerContainerLogSaver])
      dockerContainerLogSaver  ! id
    }
  }
}

