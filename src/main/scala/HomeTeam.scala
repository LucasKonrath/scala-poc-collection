class HomeTeam(val teamName: String,
               val teamPlayers: List[Player]) extends Team(teamName, teamPlayers) {
  
  override def home: Boolean = true

}
