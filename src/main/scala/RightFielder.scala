class RightFielder(name: String, number: Int, team: Team) extends Player(name, number, team) with Fielder {
  override def role: String = "Right Fielder"
  override def position: String = "Right Field"

  override def play: Boolean = true
}
