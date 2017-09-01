package surya.onGithub.actors.github

import akka.actor.{Actor, PoisonPill}
import com.google.common.io.BaseEncoding
import org.json4s._
import surya.onGithub.actors.{HookShot, OnGithubDockerImage}
import surya.onGithub.di.Services

class GithubActor(services: Services) extends Actor{
  override def receive: Receive = {
    case hookShot:HookShot => {
      import net.caoticode.buhtig.Converters._
      val repo = hookShot.repo
      val org = hookShot.org
      val onGithubFile = (services.githubClient / "repos" / hookShot.org /hookShot.repo ).contents.onGithub.get[JSON]
      val encodedContents = (onGithubFile \\ "content").values.asInstanceOf[String]
      val image = new String(BaseEncoding.base64().decode(encodedContents.trim.replaceAll("\\n","")))
      sender()! OnGithubDockerImage(hookShot.hookId, image.trim.replaceAll("\\n",""))
      self ! PoisonPill
    }
  }
}

