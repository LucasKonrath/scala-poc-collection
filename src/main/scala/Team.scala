

trait Team(var name: String, val players: List[Player]) {
    def home: Boolean

    def pitcher: Player = players.find(_.isInstanceOf[Pitcher])
      .getOrElse(throw new NoSuchElementException("No pitcher in team"))

    def catcher: Player = players.find(_.isInstanceOf[Catcher])
      .getOrElse(throw new NoSuchElementException("No catcher in team"))

    def batter: Player = players.find(_.isInstanceOf[Batter])
      .getOrElse(throw new NoSuchElementException("No batter in team"))

}
