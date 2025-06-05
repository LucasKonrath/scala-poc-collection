
trait Half(val attack: Team, val defend: Team) {
  var out: Int = 0
  var ball: Int = 0
  var strike: Int = 0

  var points: Int = 0

  var firstBase: Boolean = false
  var secondBase: Boolean = false
  var thirdBase: Boolean = false

  def pitcher: Player = defend.pitcher
  def catcher: Player = defend.catcher
  def batter: Player = attack.batter

  val rand = new scala.util.Random

  def play(): Unit = {
    val isStrikeBall = pitcher.play
    val isBatterStrike = batter.play

    if(isStrikeBall && !isBatterStrike) strike += 1
    else if(isBatterStrike) {
      val validBall = rand.nextInt() % 2 == 0
      if(!validBall) strike += 1
      val homeRun = rand.nextInt() % 17 == 0
      if(homeRun){
        if(firstBase) points += 1
        if(secondBase) points += 1
        if(thirdBase) points += 1
        points += 1
        firstBase = false
        secondBase = false
        thirdBase = false
      } else {

      }
    }
  }
}
