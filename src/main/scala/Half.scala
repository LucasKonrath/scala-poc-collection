
trait Half {  
  val attack: Team
  val defend: Team
  var out: Int = 0
  var ball: Int = 0
  var strike: Int = 0

  var points: Int = 0

  val firstBase = new FirstBase()
  val secondBase = new SecondBase()
  val thirdBase = new ThirdBase()
  val homePlate = new HomePlate()


  def pitcher: Player = defend.pitcher
  def catcher: Player = defend.catcher
  def batter: Player = attack.batter

  val rand = new scala.util.Random

  def play(): Boolean = {
    val isStrikeBall = pitcher.play
    val isBatterStrike = batter.play

    if(isStrikeBall && !isBatterStrike) strike += 1
    else if(isBatterStrike) {
      val validBall = rand.nextInt() % 2 == 0
      if(!validBall) strike += 1
      val homeRun = rand.nextInt() % 17 == 0
      if(homeRun){
        if(firstBase.occupied) points += 1
        if(secondBase.occupied) points += 1
        if(thirdBase.occupied) points += 1
        points += 1
        firstBase.occupied = false
        secondBase.occupied = false
        thirdBase.occupied = false
      } else {
      // First check if any receiver catches the ball in the air
      val receivers = List(
        defend.firstBaseman.asInstanceOf[Receiver],
        defend.secondBaseman.asInstanceOf[Receiver],
        defend.thirdBaseman.asInstanceOf[Receiver],
        defend.shortStop.asInstanceOf[Receiver],
        defend.leftFielder.asInstanceOf[Receiver],
        defend.centerFielder.asInstanceOf[Receiver],
        defend.rightFielder.asInstanceOf[Receiver]
      )

      val caughtInAir = receivers.exists(_.catchInTheAir)

      if (caughtInAir) {
        out += 1
      } else {
        // Regular hit logic if the ball wasn't caught
        val hitType = rand.nextInt(100) // 0-99
        if (hitType < 70) { // Single (70% chance)
          if(thirdBase.occupied) {
            points += 1
            thirdBase.occupied = false
          }
          if(secondBase.occupied) {
            thirdBase.occupied = true
            secondBase.occupied = false
          }
          if(firstBase.occupied) {
            secondBase.occupied = true
          }
          firstBase.occupied = true
        } else if (hitType < 85) { // Double (15% chance)
          if(thirdBase.occupied) {
            points += 1
            thirdBase.occupied = false
          }
          if(secondBase.occupied) {
            points += 1
            secondBase.occupied = false
          }
          if(firstBase.occupied) {
            thirdBase.occupied = true
            firstBase.occupied = false
          }
          secondBase.occupied = true
        } else { // Triple (15% chance)
          if(thirdBase.occupied) {
            points += 1
            thirdBase.occupied = false
          }
          if(secondBase.occupied) {
            points += 1
            secondBase.occupied = false
          }
          if(firstBase.occupied) {
            points += 1
            firstBase.occupied = false
          }
          thirdBase.occupied = true
        }
      }
    }
    } else if (!isStrikeBall){
        ball += 1
    }

    if(ball >= 4){
      out += 1
      ball = 0
      strike = 0
      advanceBases(firstBase, secondBase, thirdBase)
    } else if (strike >= 3){
      out += 1
      strike = 0
      ball = 0
    }

    if(out >= 3) {
      return false
    }

    true
  }

  def advanceBases(firstBase: Base, secondBase: SecondBase, thirdBase: ThirdBase): Unit = {
    if(firstBase.occupied) {
      if(secondBase.occupied){
        if(thirdBase.occupied){
          points += 1
        } else thirdBase.occupied = true
      } else secondBase.occupied = true
    } else firstBase.occupied = true

  }
}
