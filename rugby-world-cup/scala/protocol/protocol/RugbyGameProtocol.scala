package protocol

trait Event {
  val gameId: String
  val minute: Int
}

case class SetupEvent(gameId: String, minute: Int, home: String, away: String) extends Event
case class NonEvent(gameId: String, minute: Int) extends Event
case class StandardEvent(gameId: String, minute: Int, text: String) extends Event
case class YellowEvent(gameId: String, minute: Int, homeaway: Boolean) extends Event
