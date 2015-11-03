package sss.ui

import akka.actor.ActorSystem

trait DefaultActorSystem {
  lazy val actorSystem = DefaultActorSystem.actorSystem
}

object DefaultActorSystem {
  lazy val actorSystem = ActorSystem()
}