class SecondBaseman(name: String, number: Int, team: Team) extends Player(name, number, team) with Baseman(new SecondBase()) {
  override def role: String = "Second Baseman"

  override def play: Boolean = true
}
