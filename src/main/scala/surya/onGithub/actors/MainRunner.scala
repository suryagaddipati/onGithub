package surya.onGithub.actors

import org.json4s.native.JsonMethods.parse
import surya.onGithub.actors.github.GithubActor
import surya.onGithub.di.Services

class MainRunner(services: Services) extends BaseActor{
  override def receive: Receive = {
    case hookShot:HookShot => {
      val githubActor = getActor(classOf[GithubActor])
      githubActor ! hookShot
    }
    case image:OnGithubDockerImage => {
      getActor(classOf[DockerContainerLauncher]) ! image
    }
    case container:LaunchedContainer => {
      val dockerContainerLogSaver = getActor(classOf[DockerContainerLogSaver])
      dockerContainerLogSaver  ! container
    }
  }
}

case  class HookShot(hookId: String, event:String, payload: String){
  val payloadJson = parse(payload)
  val repo =  event match {
    case "push" =>  (payloadJson \ "repository" \ "name").values.asInstanceOf[String]
  }
  val org =  event match {
    case "push" => (payloadJson \ "repository" \"owner" \ "name").values.asInstanceOf[String]
  }
}
case class OnGithubDockerImage(hookId:String,event:String, payload: String, image: String )

case class LaunchedContainer( hookId:String, containerId: String)

