

class Pitcher(name: String, number: Int, team: Team) extends Player(name, number, team) {
  override def role: String = "Pitcher"
  
  val rand = new scala.util.Random
  
  override def play: Boolean = {
    val salt = rand.nextInt()
    salt % 2 == 0
  }
}