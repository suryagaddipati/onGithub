package surya.onGithub.di

import akka.actor.{AbstractExtensionId, ExtendedActorSystem}

class DIExtension extends AbstractExtensionId[DIExt] {
  override def createExtension(system: ExtendedActorSystem): DIExt = new DIExt
}
