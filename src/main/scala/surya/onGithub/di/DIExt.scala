package surya.onGithub.di

import akka.actor.{Actor, ExtendedActorSystem, Extension, ExtensionId, Props}
import com.spotify.docker.client.DockerClient


class DIExt extends Extension {
  var services:Services = null
  def  props(actorType: Class[_ <: Actor]): Props = Props.create(actorType, services);

  def initialize(services: Services): Unit = this.services = services
}
