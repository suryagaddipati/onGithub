package surya.onGithub.actors

import surya.onGithub.actors.github.{GithubActor, HookShot}
import surya.onGithub.di.Services

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
