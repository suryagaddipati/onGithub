package surya.onGithub

import com.typesafe.config._

class Config(config: com.typesafe.config.Config) {
  config.checkValid(ConfigFactory.defaultReference())
  def this() {
    this(ConfigFactory.load())
  }
  val githubToken = config.getString("github.token")
  val githubApiUrl = config.getString("github.api-url")

  val httpHost = config.getString("http.host")
  val httpPort = config.getInt("http.port")
}
