import scala.util.Random
import scala.reflect.Selectable.reflectiveSelectable

class GameEngine {
  private val random = new Random()

  def createMockTeams(): (HomeTeam, AwayTeam) = {

    val giantsPlayers = createMockPlayers("Giants")
    val giants = new HomeTeam("Giants", giantsPlayers)
    giantsPlayers.foreach(p => p.asInstanceOf[{ def team_=(t: Team): Unit }].team_=(giants))

    val tigersPlayers = createMockPlayers("Tigers")
    val tigers = new AwayTeam("Tigers", tigersPlayers)
    tigersPlayers.foreach(p => p.asInstanceOf[{ def team_=(t: Team): Unit }].team_=(tigers))

    (giants, tigers)
  }

  private def createMockPlayers(teamName: String): List[Player] = {
    val playerNames = List(
      "John", "Michael", "David", "Robert", "James", 
      "William", "Richard", "Joseph", "Thomas", "Christopher"
    )

    val nullTeam: Team = null

    val players = List(
      new Pitcher(s"${playerNames(0)} ${teamName}", 1, nullTeam),
      new Catcher(s"${playerNames(1)} ${teamName}", 2, nullTeam),
      new FirstBaseman(s"${playerNames(2)} ${teamName}", 3, nullTeam),
      new SecondBaseman(s"${playerNames(3)} ${teamName}", 4, nullTeam),
      new ThirdBaseman(s"${playerNames(4)} ${teamName}", 5, nullTeam),
      new ShortStop(s"${playerNames(5)} ${teamName}", 6, nullTeam),
      new LeftFielder(s"${playerNames(6)} ${teamName}", 7, nullTeam),
      new CenterFielder(s"${playerNames(7)} ${teamName}", 8, nullTeam),
      new RightFielder(s"${playerNames(8)} ${teamName}", 9, nullTeam),
      // Each team needs at least one batter
      new Batter(s"${playerNames(9)} ${teamName}", 10, nullTeam)
    )

    players
  }

  def simulateGame(): Unit = {
    println("Starting Baseball Game Simulation")
    println("===================================\n")

    val (homeTeam, awayTeam) = createMockTeams()

    println(s"Today's Game: ${homeTeam.name} vs ${awayTeam.name}\n")

    var homeScore = 0
    var awayScore = 0

    for (inning <- 1 to 9) {
      println(s"\nInning $inning:")
      println("----------")

      // Top half (away team batting)
      println(s"Top of inning $inning - ${awayTeam.name} batting:")
      val topHalf = new Half {
        val attack = awayTeam
        val defend = homeTeam
      }
      val topPoints = simulateHalfInning(topHalf)
      awayScore += topPoints
      println(s"${awayTeam.name} scored $topPoints runs this half")

      // Bottom half (home team batting)
      println(s"\nBottom of inning $inning - ${homeTeam.name} batting:")
      val bottomHalf = new Half {
        val attack = homeTeam
        val defend = awayTeam
      }
      val bottomPoints = simulateHalfInning(bottomHalf)
      homeScore += bottomPoints
      println(s"${homeTeam.name} scored $bottomPoints runs this half")

      // Print current score
      println(s"\nScore after inning $inning: ${homeTeam.name} $homeScore - $awayScore ${awayTeam.name}")
    }

    // Print final results
    println("\n===================================")
    println("Game Over!")
    println(s"Final Score: ${homeTeam.name} $homeScore - $awayScore ${awayTeam.name}")

    if (homeScore > awayScore) {
      println(s"${homeTeam.name} win!")
    } else if (awayScore > homeScore) {
      println(s"${awayTeam.name} win!")
    } else {
      println("It's a tie!")
    }
  }

  private def simulateHalfInning(half: Half): Int = {
    val startingPoints = half.points
    var playResult = true

    while (playResult) {
      playResult = half.play()

      if (half.strike == 1) println("Strike one!")
      if (half.strike == 2) println("Strike two!")
      if (half.ball == 1) println("Ball one!")
      if (half.ball == 2) println("Ball two!")
      if (half.ball == 3) println("Ball three!")

      if (half.out == 1) println("One out!")
      if (half.out == 2) println("Two outs!")
      if (half.out == 3) println("Three outs, changing sides!")

      if (half.firstBase.occupied) println("Runner on first!")
      if (half.secondBase.occupied) println("Runner on second!")
      if (half.thirdBase.occupied) println("Runner on third!")
    }

    half.points - startingPoints
  }

  def runSimulation(): Unit = {
    simulateGame()
  }
}

object BaseballSimulation {
  def main(args: Array[String]): Unit = {
    val engine = new GameEngine()
    engine.runSimulation()
  }
}
