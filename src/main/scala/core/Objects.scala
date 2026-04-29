package core

// Singleton object
object MathUtils:
  val PI: Double = 3.14159265358979
  val E: Double = 2.71828182845905

  def square(x: Double): Double = x * x
  def cube(x: Double): Double = x * x * x
  def clamp(value: Double, min: Double, max: Double): Double =
    Math.max(min, Math.min(max, value))

// Companion object (same name as a class)
class Circle2(val radius: Double):
  def area: Double = Circle2.PI * radius * radius
  def circumference: Double = 2 * Circle2.PI * radius
  override def toString: String = s"Circle2(radius=$radius)"

object Circle2:
  val PI: Double = 3.14159265358979

  // Factory methods
  def apply(radius: Double): Circle2 = new Circle2(radius)
  def unit: Circle2 = new Circle2(1.0)
  def fromDiameter(diameter: Double): Circle2 = new Circle2(diameter / 2)
  def fromArea(area: Double): Circle2 = new Circle2(Math.sqrt(area / PI))

// Object extending a trait
trait Serializer[T]:
  def serialize(value: T): String
  def deserialize(str: String): T

object IntSerializer extends Serializer[Int]:
  def serialize(value: Int): String = value.toString
  def deserialize(str: String): Int = str.toInt

// Object as enum-like pattern (pre Scala 3 enums)
object Direction:
  val North = "N"
  val South = "S"
  val East = "E"
  val West = "W"
  val all: List[String] = List(North, South, East, West)

// Object with private state (module pattern)
object IdGenerator:
  private var current: Long = 0
  def next(): Long =
    current += 1
    current
  def reset(): Unit = current = 0

// Object with apply method (callable object)
object Greeting:
  def apply(name: String): String = s"Hello, $name!"
  def apply(name: String, time: String): String = s"Good $time, $name!"

// Utility object with extension methods
object StringOps:
  extension (s: String)
    def shout: String = s.toUpperCase + "!!!"
    def whisper: String = s.toLowerCase + "..."
    def repeat(n: Int): String = s * n
    def isPalindrome: Boolean = s == s.reverse

@main def objectsPoc(): Unit =
  println("=== Scala Objects POC ===")

  // Singleton usage
  println(s"MathUtils.PI = ${MathUtils.PI}")
  println(s"MathUtils.square(5) = ${MathUtils.square(5)}")
  println(s"MathUtils.clamp(15, 0, 10) = ${MathUtils.clamp(15, 0, 10)}")

  // Companion object factory methods
  println("\n=== Companion Object ===")
  val c1 = Circle2(5.0)           // uses apply
  val c2 = Circle2.unit           // named factory
  val c3 = Circle2.fromDiameter(10.0)
  val c4 = Circle2.fromArea(78.54)
  println(s"Circle2(5): area=${c1.area}")
  println(s"Circle2.unit: $c2")
  println(s"Circle2.fromDiameter(10): $c3")
  println(s"Circle2.fromArea(78.54): $c4")

  // Object extending trait
  println("\n=== Object as Trait Implementation ===")
  val serialized = IntSerializer.serialize(42)
  val deserialized = IntSerializer.deserialize(serialized)
  println(s"Serialized: $serialized, Deserialized: $deserialized")

  // Enum-like object
  println("\n=== Enum-like Object ===")
  println(s"Directions: ${Direction.all}")

  // Stateful object
  println("\n=== Stateful Object ===")
  println(s"ID: ${IdGenerator.next()}")
  println(s"ID: ${IdGenerator.next()}")
  println(s"ID: ${IdGenerator.next()}")

  // Callable object
  println("\n=== Callable Object (apply) ===")
  println(Greeting("World"))
  println(Greeting("Alice", "morning"))

  // Extension methods
  println("\n=== Extension Methods ===")
  import StringOps.*
  println(s"\"hello\".shout = ${"hello".shout}")
  println(s"\"HELLO\".whisper = ${"HELLO".whisper}")
  println(s"\"ha\".repeat(3) = ${"ha".repeat(3)}")
  println(s"\"racecar\".isPalindrome = ${"racecar".isPalindrome}")
  println(s"\"hello\".isPalindrome = ${"hello".isPalindrome}")
