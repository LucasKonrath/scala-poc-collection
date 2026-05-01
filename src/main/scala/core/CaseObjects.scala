package core

// Case objects are singletons with auto-generated toString, hashCode, equals
// Perfect for representing values with no parameters

// Case object as singleton value
case object DefaultTimeout:
  val millis: Long = 5000

// Case objects as enumeration values
trait Color
case object Red extends Color
case object Green extends Color
case object Blue extends Color
case object Yellow extends Color

// Case objects mixed with case classes in an ADT
trait HttpMethod
case object GET extends HttpMethod
case object POST extends HttpMethod
case object PUT extends HttpMethod
case object DELETE extends HttpMethod
case object PATCH extends HttpMethod

// Case objects representing states
trait ConnectionState
case object Connected extends ConnectionState
case object Disconnected extends ConnectionState
case object Connecting extends ConnectionState
case class Failed(reason: String) extends ConnectionState

// Case objects as commands/messages (actor-like pattern)
trait Command
case object Start extends Command
case object Stop extends Command
case object Pause extends Command
case object Resume extends Command
case class SendMessage(content: String) extends Command

// Case object vs regular object comparison
case object CaseVersion
object RegularVersion

// Case objects in a linked list
trait MyList[+A]:
  def head: A
  def tail: MyList[A]
  def isEmpty: Boolean
  def ::[B >: A](elem: B): MyList[B] = Cons(elem, this)

case object Empty extends MyList[Nothing]:
  def head = throw new NoSuchElementException("empty list")
  def tail = throw new NoSuchElementException("empty list")
  def isEmpty = true
  override def toString = "Empty"

case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]:
  def isEmpty = false
  override def toString = s"$head :: $tail"

@main def caseObjectsPoc(): Unit =
  println("=== Scala Case Objects POC ===")

  // Case object singleton
  println(s"DefaultTimeout: $DefaultTimeout")
  println(s"DefaultTimeout.millis: ${DefaultTimeout.millis}")

  // Case objects as enum values
  println("\n=== Color Enum ===")
  val colors: List[Color] = List(Red, Green, Blue, Yellow)
  colors.foreach(c => println(s"  Color: $c"))

  // Pattern matching on case objects
  println("\n=== Pattern Matching ===")
  def handleMethod(method: HttpMethod): String = method match
    case GET    => "Fetching resource"
    case POST   => "Creating resource"
    case PUT    => "Updating resource"
    case DELETE => "Deleting resource"
    case PATCH  => "Partially updating resource"

  val methods: List[HttpMethod] = List(GET, POST, PUT, DELETE, PATCH)
  methods.foreach(m => println(s"  $m -> ${handleMethod(m)}"))

  // Mixed case objects and case classes
  println("\n=== Connection States ===")
  def describeState(state: ConnectionState): String = state match
    case Connected    => "Connection established"
    case Disconnected => "Not connected"
    case Connecting   => "Attempting to connect..."
    case Failed(reason) => s"Connection failed: $reason"

  val states: List[ConnectionState] = List(Connecting, Connected, Failed("timeout"), Disconnected)
  states.foreach(s => println(s"  $s -> ${describeState(s)}"))

  // Case object identity
  println("\n=== Identity & Equality ===")
  val a: Color = Red
  val b: Color = Red
  println(s"Red == Red: ${a == b}")         // true
  println(s"Red eq Red: ${a eq b}")         // true (same instance!)
  println(s"Red.hashCode: ${Red.hashCode}")

  // Case object serialization-friendly toString
  println(s"\nCaseVersion.toString: ${CaseVersion.toString}")
  println(s"RegularVersion.toString: ${RegularVersion.toString}")

  // Custom linked list using case object Empty
  println("\n=== Custom List with Case Object ===")
  val myList = 1 :: 2 :: 3 :: Empty
  println(s"myList: $myList")
  println(s"myList.head: ${myList.head}")
  println(s"myList.tail: ${myList.tail}")
  println(s"Empty.isEmpty: ${Empty.isEmpty}")

  // Commands pattern
  println("\n=== Command Pattern ===")
  def executeCommand(cmd: Command): String = cmd match
    case Start              => "System starting..."
    case Stop               => "System stopping..."
    case Pause              => "System paused"
    case Resume             => "System resumed"
    case SendMessage(content) => s"Sending: $content"

  val commands: List[Command] = List(Start, SendMessage("Hello"), Pause, Resume, Stop)
  commands.foreach(c => println(s"  $c -> ${executeCommand(c)}"))
