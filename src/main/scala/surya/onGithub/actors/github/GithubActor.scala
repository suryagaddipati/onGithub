package surya.onGithub.actors.github

import akka.actor.Actor
import com.google.common.io.BaseEncoding
import surya.onGithub.di.Services

class GithubActor(services: Services) extends Actor{
  override def receive: Receive = {
    case hookShot:HookShot => {
//
      import net.caoticode.buhtig.Converters._
      val repo = hookShot.repo
      val org = hookShot.org
      val onGithubFile = (services.githubClient / "repos" / hookShot.org /hookShot.repo ).contents.onGithub.get[JSON]
      val encodedContents = (onGithubFile \\ "content").values.asInstanceOf[String]
      val image = new String(BaseEncoding.base64().decode(encodedContents.trim.replaceAll("\\n","")))
      sender()! image.trim.replaceAll("\\n","")

    }
  }
}
case  class HookShot(hookId: String, event:String,payload: String){
  def repo:String = "pulihora"
  def org:String = "suryagaddipati"
}
