class CenterFielder(name: String, number: Int, team: Team) extends Player(name, number, team) with Fielder {
  override def role: String = "Center Fielder"
  override def position: String = "Center Field"

  override def play: Boolean = true
}
