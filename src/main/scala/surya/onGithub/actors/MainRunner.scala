package surya.onGithub.actors

import akka.actor.Actor
import surya.onGithub.actors.github.HookShot
import surya.onGithub.di.Services

class MainRunner(services: Services) extends Actor{
  override def receive: Receive = {
    case hookShot:HookShot => {


    }
  }
}
