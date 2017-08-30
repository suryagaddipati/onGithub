package surya.onGithub.actors

import akka.pattern.ask
import akka.util.Timeout
import surya.onGithub.actors.github.{GithubActor, HookShot}
import surya.onGithub.di.Services

import scala.concurrent.duration.Duration
import scala.concurrent.duration._

class MainRunner(services: Services) extends BaseActor{
  override def receive: Receive = {
    case hookShot:HookShot => {
      val githubActor = getActor(classOf[GithubActor])
      githubActor ! hookShot
    }
    case image:String => {
      getActor(classOf[DockerContainerLauncher]) ! image
    }
  }
}
