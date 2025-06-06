class ThirdBaseman(name: String, number: Int, team: Team) extends Player(name, number, team) with Baseman(new ThirdBase()) {
  override def role: String = "Third Baseman"

  override def play: Boolean = true
}
