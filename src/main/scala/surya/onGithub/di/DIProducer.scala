package surya.onGithub.di

import akka.actor.{Actor, IndirectActorProducer}
import surya.onGithub.actors.{DockerContainerLogSaver, DockerContainerLauncher}

class DIProducer(services: Services, actorType: Class[Actor]) extends IndirectActorProducer {
  override def actorClass = classOf[Actor]
  override def produce = actorType.getConstructor(classOf[Services]).newInstance(services)
}
