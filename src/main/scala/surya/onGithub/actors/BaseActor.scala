package surya.onGithub.actors

import akka.actor.Actor
import surya.onGithub.di.DI

abstract class BaseActor extends Actor{
  def getActor[T <: Actor](m: Class[T]) =  context.system.actorOf(DI.instance.get(context.system).props(m))
}
