class ShortStop(name: String, number: Int, team: Team) extends Player(name, number, team) with Receiver {
  override def role: String = "Short Stop"

  override def play: Boolean = true
}
