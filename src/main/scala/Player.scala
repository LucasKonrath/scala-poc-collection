trait Player(val name: String, val number: Int, val team: Team) {
  def role: String
  def play: Boolean
}