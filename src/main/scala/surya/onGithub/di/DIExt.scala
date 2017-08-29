package surya.onGithub.di

import akka.actor.{Actor, ExtendedActorSystem, Extension, ExtensionId, Props}
import com.spotify.docker.client.DockerClient


class DIExt extends Extension {
  var dependDencies:Services = null


  //    def props(actorType: Type): Props = Props.create(classOf[DIProducer],null , actorType)
  //  override def createExtension(system: ExtendedActorSystem): DIExt = ???

  //  def props(actorType: Class[Actor]): Props = Props.create(classOf[DIProducer],  actorType)
  def  props[T<: Actor](actorType: Class[T]): Props = Props.create(classOf[DIProducer], dependDencies, actorType);

  def initialize(dependencies: Services): Unit ={
    this.dependDencies = dependencies
  }
}
