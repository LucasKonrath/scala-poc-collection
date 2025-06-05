

class Catcher(name: String, number: Int, team: Team) extends Player(name, number, team) {
  override def role: String = "Catcher"
  
  override def play: Boolean = true
}