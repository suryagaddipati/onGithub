package surya.onGithub.actors.github

import akka.actor.Actor
import surya.onGithub.di.Services

class GithubActor(services: Services) extends Actor{
  override def receive: Receive = {
    case hookShot:HookShot => {


    }
  }
}
case  class HookShot(id: String, event:String,payload: String){
  def repo:String = ""
}
