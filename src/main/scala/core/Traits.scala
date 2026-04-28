package core

// Basic trait
trait Greeter:
  def greet(name: String): String

// Trait with default implementation
trait Logger:
  def log(message: String): Unit = println(s"[LOG] $message")
  def warn(message: String): Unit = println(s"[WARN] $message")
  def error(message: String): Unit = println(s"[ERROR] $message")

// Trait with abstract and concrete members
trait Describable:
  def name: String // abstract
  def description: String = s"I am $name" // concrete with default

// Multiple trait inheritance (mixins)
trait Swimmer:
  def swim(): String = s"${this} is swimming"

trait Runner:
  def run(): String = s"${this} is running"

trait Flyer:
  def fly(): String = s"${this} is flying"

class Duck(val name: String) extends Swimmer with Runner with Flyer:
  override def toString: String = name

class Penguin(val name: String) extends Swimmer with Runner:
  override def toString: String = name

// Trait with self-type (requires specific type)
trait HasId:
  def id: Int

trait Auditable:
  this: HasId => // requires HasId to be mixed in
  def audit(): String = s"Auditing entity with id=$id"

class User(val id: Int, val username: String) extends HasId with Auditable:
  override def toString: String = s"User($id, $username)"

// Trait parameters (Scala 3 feature)
trait Printable(val prefix: String):
  def print(msg: String): Unit = println(s"$prefix: $msg")

class ConsolePrinter extends Printable("CONSOLE")

// Stacking traits (linearization)
trait Base:
  def operation: String = "base"

trait First extends Base:
  override def operation: String = s"first -> ${super.operation}"

trait Second extends Base:
  override def operation: String = s"second -> ${super.operation}"

class Diamond extends Base with First with Second

// Trait as type bound
trait Summable[T]:
  def sum(a: T, b: T): T

given Summable[Int] with
  def sum(a: Int, b: Int): Int = a + b

given Summable[String] with
  def sum(a: String, b: String): String = a + b

def addThings[T](a: T, b: T)(using s: Summable[T]): T = s.sum(a, b)

@main def traitsPoc(): Unit =
  println("=== Scala Traits POC ===")

  // Anonymous implementation of trait
  val politeGreeter = new Greeter:
    def greet(name: String): String = s"Good day, $name! How do you do?"
  println(politeGreeter.greet("World"))

  // Mixin traits
  println("\n=== Mixins ===")
  val duck = Duck("Donald")
  println(duck.swim())
  println(duck.run())
  println(duck.fly())

  val penguin = Penguin("Tux")
  println(penguin.swim())
  println(penguin.run())
  // penguin.fly() - won't compile, Penguin doesn't extend Flyer

  // Logger trait with default implementation
  println("\n=== Logger Trait ===")
  val service = new Object with Logger
  service.log("Application started")
  service.warn("Memory usage high")
  service.error("Connection failed")

  // Describable trait
  println("\n=== Describable ===")
  val item = new Describable { val name = "Widget" }
  println(item.description)

  // Self-type
  println("\n=== Self-Type ===")
  val user = User(42, "alice")
  println(user.audit())

  // Trait parameters (Scala 3)
  println("\n=== Trait Parameters ===")
  val printer = ConsolePrinter()
  printer.print("Hello from Scala 3 trait parameters!")

  // Linearization
  println("\n=== Trait Linearization ===")
  val diamond = Diamond()
  println(s"Diamond operation: ${diamond.operation}")
  // Linearization order: Diamond -> Second -> First -> Base
  // So: second -> first -> base

  // Type class pattern with traits
  println("\n=== Type Class Pattern ===")
  println(s"addThings(3, 4) = ${addThings(3, 4)}")
  println(s"addThings(\"hello\", \" world\") = ${addThings("hello", " world")}")
