package surya.onGithub.di

import akka.actor.{Actor, IndirectActorProducer}
import surya.onGithub.actors.{DockerContainerLogSaver, DockerContainerLauncher}

class DIProducer(dependencies: Services, actorType: Class[_]) extends IndirectActorProducer {
  val LAUNCHER = classOf[DockerContainerLauncher]
  val SAVER = classOf[DockerContainerLogSaver]
  override def actorClass = classOf[Actor]
  override def produce =  actorType match {
    case LAUNCHER => new DockerContainerLauncher(dependencies.dockerClient)
    case SAVER => new DockerContainerLogSaver(dependencies.dockerClient,dependencies.mongoDB)
  }

}
