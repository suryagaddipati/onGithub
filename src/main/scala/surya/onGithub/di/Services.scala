package surya.onGithub.di

import com.spotify.docker.client.DockerClient
import net.caoticode.buhtig.SyncClient
import org.mongodb.scala.MongoDatabase


case class Services(dockerClient: DockerClient, mongoDB: MongoDatabase, githubClient: SyncClient)

