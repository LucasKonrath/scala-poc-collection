class LeftFielder(name: String, number: Int, team: Team) extends Player(name, number, team) with Fielder {
  override def role: String = "Left Fielder"
  override def position: String = "Left Field"

  override def play: Boolean = true
}
