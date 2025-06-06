

class Catcher(name: String, number: Int, team: Team) extends Player(name, number, team) with Receiver {
  override def role: String = "Catcher"
  
  override def play: Boolean = true
}