
import scala.io.Source
import java.io.File

object Main {

  def main(args: Array[String]) {

     val buffered = Source.fromFile(new File(args(0)))
    try {
      buffered.getLines.foreach(generateEvent(_))
    } finally {
      buffered.close
    }

  }

  def generateEvent(line: String) {
    line.split(",")(0) match {
      case "I" => println(s"$line is an init event")
      case _ => println(s"$line is an unknown event")
    }
  }
}