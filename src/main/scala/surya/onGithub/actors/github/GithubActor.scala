package surya.onGithub.actors.github

import akka.actor.Actor
import surya.onGithub.di.Services

class GithubActor(services: Services) extends Actor{
  override def receive: Receive = {
    case image:String => {

      //      val config = ContainerConfig.builder.image(image).cmd("sh","-c","while :; do echo `date`; sleep 3; done").build
      //      val creation = services.dockerClient.createContainer(config)
      //      val id = creation.id
      //      services.dockerClient.startContainer(id)
      //      val dockerContainerLogSaver = context.system.actorOf(DI.instance.get(context.system).props(classOf[DockerContainerLogSaver]))
      //      dockerContainerLogSaver  ! id

    }
  }
}
case  class HookShot(id: String, event:String,payload: String)
