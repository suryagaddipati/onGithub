package surya.onGithub.actors.github

import dispatch.{Http, Req, host}
import net.caoticode.buhtig.{AsyncClient, SyncClient}
class BhutigAPI(token:String,apiServer: String){
    val authHeader = Map("Authorization" -> s"token $token")
    val github: Req = host(apiServer).secure <:< authHeader
    def syncClient = new SyncClient(github)
    def asyncClient = new AsyncClient(github)

    def close() = Http.shutdown()
  }
