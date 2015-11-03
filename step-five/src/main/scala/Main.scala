
import akka.actor._
import protocol._
import protocol.YellowEvent
import protocol.SetupEvent

object Main extends App {

  println("Hello wrold")
  implicit val system = ActorSystem("LocalSystem")
  val remote = system.actorSelection("akka.tcp://default@127.0.0.1:2577/user/rugbyGameEventsink")

  remote ! "Can you feel it?"

  remote ! SetupEvent("att2", 10, "Ireland", "New Zeland")
  //remote ! YellowEvent("att2", 50, true)

}
