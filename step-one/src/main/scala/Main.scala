
import scala.io.Source
import java.io.File
import scala.util.Try

object Main {

  def main(args: Array[String]) {

    val buffered = Source.fromFile(new File(args(0)))
    try {
      buffered.getLines.foreach(println(_))
    } finally {
      buffered.close
    }
  }
}