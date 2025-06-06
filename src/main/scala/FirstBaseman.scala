class FirstBaseman(name: String, number: Int, team: Team) extends Player(name, number, team) with Baseman(new FirstBase()) {
  override def role: String = "First Baseman"

  override def play: Boolean = true
}
