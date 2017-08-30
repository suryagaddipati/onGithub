package surya.onGithub.actors.github

import akka.actor.{Actor, PoisonPill}
import com.google.common.io.BaseEncoding
import surya.onGithub.di.Services
import org.json4s._
import org.json4s.native.JsonMethods._

class GithubActor(services: Services) extends Actor{
  override def receive: Receive = {
    case hookShot:HookShot => {
      import net.caoticode.buhtig.Converters._
      val repo = hookShot.repo
      val org = hookShot.org
      val onGithubFile = (services.githubClient / "repos" / hookShot.org /hookShot.repo ).contents.onGithub.get[JSON]
      val encodedContents = (onGithubFile \\ "content").values.asInstanceOf[String]
      val image = new String(BaseEncoding.base64().decode(encodedContents.trim.replaceAll("\\n","")))
      sender()! image.trim.replaceAll("\\n","")
      self ! PoisonPill
    }
  }
}
case  class HookShot(hookId: String, event:String,payload: String){
  val payloadJson = parse(payload)
  val repo =  event match {
    case "push" =>  (payloadJson \ "repository" \ "name").values.asInstanceOf[String]
  }
  val org =  event match {
    case "push" => (payloadJson \ "repository" \"owner" \ "name").values.asInstanceOf[String]
  }

}
