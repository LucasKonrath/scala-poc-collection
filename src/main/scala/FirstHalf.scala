class FirstHalf(val homeTeam: HomeTeam, val awayTeam: AwayTeam) extends Half {
  val attack = awayTeam
  val defend = homeTeam
}
