package surya.onGithub.actors

import java.io.{PipedInputStream, PipedOutputStream}

import akka.actor.Actor
import com.spotify.docker.client.DockerClient.AttachParameter
import org.mongodb.scala.gridfs.helpers.AsyncStreamHelper._
import org.mongodb.scala.gridfs.{AsyncInputStream, GridFSBucket, GridFSUploadOptions}
import surya.onGithub.di.Services

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
class DockerContainerLogSaver(services: Services)extends Actor{
  override def receive: Receive ={
    case launchedContainer:LaunchedContainer => {

      val mongoStream = new PipedInputStream()
      val dockerOuputStream = new PipedOutputStream(mongoStream)
      Future{
        services.dockerClient
          .attachContainer(launchedContainer.containerId,
            AttachParameter.LOGS, AttachParameter.STDOUT,
            AttachParameter.STDERR, AttachParameter.STREAM)
          .attach(dockerOuputStream,dockerOuputStream )
      }

      val gridFSBucket = GridFSBucket(services.mongoDB)
      val streamToUploadFrom: AsyncInputStream = toAsyncInputStream(mongoStream)

      val options: GridFSUploadOptions = new GridFSUploadOptions()//.chunkSizeBytes(10)


      gridFSBucket.uploadFromStream(launchedContainer.hookId, streamToUploadFrom,options).head()
      streamToUploadFrom.close()
    }
  }
}
