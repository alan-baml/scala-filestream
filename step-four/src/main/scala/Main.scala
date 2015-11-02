
import scala.io.Source
import java.io.File

case class GenericEvent(kind: String, eventData: Seq[String])

trait Event
case class InitEvent(home:String, away: String) extends Event
case object UnknownEvent extends Event

object Main {

  def main(args: Array[String]) {

    val buffered = Source.fromFile(new File(args(0)))

    try {
      val linesAsEvents = for {
        line <- buffered.getLines
        splitOnComma = line.split(",")
        lineAsMap = (splitOnComma(0) -> splitOnComma.tail)
        lineAsGenericEvent = GenericEvent(lineAsMap._1, lineAsMap._2)
        lineAsEvent = matchEvent(lineAsGenericEvent)
      } yield lineAsEvent

      linesAsEvents foreach (println(_))

    } finally {
      buffered.close
    }

  }

  def matchEvent(lineAsGenericEvent: GenericEvent): Event =  lineAsGenericEvent match {
    case GenericEvent("I", eventData) => InitEvent(eventData(0), eventData(1))
    case GenericEvent(_, eventData) => UnknownEvent
  }
}