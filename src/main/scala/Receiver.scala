trait Receiver extends Player {

    private val rand = new scala.util.Random

    def receive: Boolean = {
      def salt: Int = rand.nextInt()
      salt % 3 == 0 || salt % 2 == 0
    }

    def catchInTheAir: Boolean = {
      def salt: Int = rand.nextInt()
      salt % 5 == 0
    }

    def throwBall(receiver: Receiver): Boolean = {
      receiver.receive
    }
}
