
import scala.io.Source
import java.io.File

case class GenericEvent(kind: String, eventData: Seq[String])

object Main {

  def main(args: Array[String]) {

     val buffered = Source.fromFile(new File(args(0)))
    try {
      val linesAsMap = buffered.getLines.map(generateEvent(_))
      val linesAsGenericEvents = linesAsMap map { kvPair =>
        GenericEvent(kvPair._1, kvPair._2)
      }
      linesAsGenericEvents foreach (e => println(s"KEY -  ${e}"))
    } finally {
      buffered.close
    }

  }

  def generateEvent(line: String): (String, Seq[String]) = {
    val splitOnComma = line.split(",")
      (splitOnComma(0) -> splitOnComma.tail)
  }
}