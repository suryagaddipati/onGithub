package surya.onGithub.actors

import akka.actor.Actor
import surya.onGithub.actors.github.{GithubActor, HookShot}
import surya.onGithub.di.Services

class MainRunner(services: Services) extends BaseActor{
  override def receive: Receive = {
    case hookShot:HookShot => {
      val githubActor = getActor(classOf[GithubActor])
       githubActor.tell() ? hookShot
    }
  }
}
