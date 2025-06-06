trait Player(val name: String, val number: Int, var team: Team) {
  def role: String
  def play: Boolean
}