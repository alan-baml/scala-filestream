
import akka.actor._
import protocol._
import scala.io.Source
import java.io.File
import scala.concurrent.duration.FiniteDuration
import scala.concurrent.Promise
import scala.concurrent.Await
import scala.concurrent.duration.Duration

case class GenericEvent(kind: String, eventData: Seq[String])

object Main extends App {

  val system = ActorSystem("LocalSystem")
  val finishGate = Promise[Unit]()
  val timedActorRef = system.actorOf(Props(classOf[TimedActor], finishGate))

  val buffered = Source.fromFile(new File(args(0)))

  try {
    val linesAsEvents = for {
      line <- buffered.getLines
      splitOnComma = line.split(",")
      lineAsMap = (splitOnComma(0) -> splitOnComma.tail)
      lineAsGenericEvent = GenericEvent(lineAsMap._1, lineAsMap._2)
      lineAsEvent = matchEvent(lineAsGenericEvent)
    } yield lineAsEvent

    timedActorRef ! Game(linesAsEvents.toSeq)

    Await.result(finishGate.future, Duration.Inf)

  } finally {
    buffered.close
    system.terminate
  }

  def matchEvent(lineAsGenericEvent: GenericEvent): Event = lineAsGenericEvent match {
    case GenericEvent("S", eventData) => SetupEvent(eventData(0), eventData(1).toInt, eventData(2), eventData(3))
    case GenericEvent("N", eventData) => NonEvent(eventData(0), eventData(1).toInt)
    case GenericEvent("E", eventData) => StandardEvent(eventData(0), eventData(1).toInt, eventData(2))
    case GenericEvent("Y", eventData) => YellowEvent(eventData(0), eventData(1).toInt, eventData(2) == "home")
  }

}

case class Game(events: Seq[Event])

class TimedActor(terminate: Promise[Unit]) extends Actor {

  import scala.concurrent.duration._
  private val interval: FiniteDuration = 1 second

  import scala.concurrent.ExecutionContext.Implicits.global

  val remote = context.system.actorSelection("akka.tcp://default@127.0.0.1:2577/user/rugbyGameEventsink")

  def receive: Receive = {
    case Game(events) if !events.isEmpty => {
      remote ! events.head
      context.system.scheduler.scheduleOnce(interval, self, Game(events.tail))
    }

    case Game(_) => terminate.success()

  }

}