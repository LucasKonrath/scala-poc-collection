

trait Team(var name: String, val players: List[Player]) {
    def home: Boolean

    def pitcher: Player = players.find(_.isInstanceOf[Pitcher])
      .getOrElse(throw new NoSuchElementException("No pitcher in team"))

    def catcher: Player = players.find(_.isInstanceOf[Catcher])
      .getOrElse(throw new NoSuchElementException("No catcher in team"))

    def batter: Player = players.find(_.isInstanceOf[Batter])
      .getOrElse(throw new NoSuchElementException("No batter in team"))

    def firstBaseman: Player = players.find(_.isInstanceOf[FirstBaseman])
      .getOrElse(throw new NoSuchElementException("No first baseman in team"))

    def secondBaseman: Player = players.find(_.isInstanceOf[SecondBaseman])
      .getOrElse(throw new NoSuchElementException("No second baseman in team"))

    def thirdBaseman: Player = players.find(_.isInstanceOf[ThirdBaseman])
      .getOrElse(throw new NoSuchElementException("No third baseman in team"))

    def shortStop: Player = players.find(_.isInstanceOf[ShortStop])
      .getOrElse(throw new NoSuchElementException("No short stop in team"))

    def leftFielder: Player = players.find(_.isInstanceOf[LeftFielder])
      .getOrElse(throw new NoSuchElementException("No left fielder in team"))

    def centerFielder: Player = players.find(_.isInstanceOf[CenterFielder])
      .getOrElse(throw new NoSuchElementException("No center fielder in team"))

    def rightFielder: Player = players.find(_.isInstanceOf[RightFielder])
      .getOrElse(throw new NoSuchElementException("No right fielder in team"))
}
