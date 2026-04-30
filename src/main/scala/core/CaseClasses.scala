package core

// Basic case class - immutable by default, auto-generates equals, hashCode, toString, copy
case class Person2(name: String, age: Int)

// Case class with default values
case class Config(host: String = "localhost", port: Int = 8080, debug: Boolean = false)

// Nested case classes
case class Address(street: String, city: String, country: String)
case class Employee(name: String, role: String, address: Address)

// Generic case class
case class Pair[A, B](first: A, second: B)

// Case class with methods
case class Vector2D(x: Double, y: Double):
  def magnitude: Double = Math.sqrt(x * x + y * y)
  def +(other: Vector2D): Vector2D = Vector2D(x + other.x, y + other.y)
  def *(scalar: Double): Vector2D = Vector2D(x * scalar, y * scalar)
  def normalized: Vector2D =
    val mag = magnitude
    if mag == 0 then this else Vector2D(x / mag, y / mag)

@main def caseClassesPoc(): Unit =
  println("=== Scala Case Classes POC ===")

  // Auto-generated apply (no 'new' needed)
  val alice = Person2("Alice", 30)
  val bob = Person2("Bob", 25)
  println(s"Alice: $alice")
  println(s"Bob: $bob")

  // Auto-generated equals (structural equality)
  val alice2 = Person2("Alice", 30)
  println(s"\nalice == alice2: ${alice == alice2}")       // true (structural)
  println(s"alice eq alice2: ${alice eq alice2}")         // false (reference)
  println(s"alice == bob: ${alice == bob}")               // false

  // Auto-generated hashCode (consistent with equals)
  println(s"\nalice.hashCode == alice2.hashCode: ${alice.hashCode == alice2.hashCode}")
  val personSet = Set(alice, alice2, bob)
  println(s"Set(alice, alice2, bob).size = ${personSet.size}") // 2, not 3

  // Copy method (creates modified copy)
  println("\n=== Copy ===")
  val olderAlice = alice.copy(age = 31)
  val renamedBob = bob.copy(name = "Robert")
  println(s"olderAlice: $olderAlice")
  println(s"renamedBob: $renamedBob")
  println(s"Original alice unchanged: $alice")

  // Default values
  println("\n=== Default Values ===")
  val defaultConfig = Config()
  val prodConfig = Config(host = "prod.example.com", port = 443, debug = false)
  val debugConfig = defaultConfig.copy(debug = true)
  println(s"Default: $defaultConfig")
  println(s"Prod: $prodConfig")
  println(s"Debug: $debugConfig")

  // Pattern matching (unapply auto-generated)
  println("\n=== Pattern Matching ===")
  def describe(p: Person2): String = p match
    case Person2("Alice", age) => s"It's Alice, age $age"
    case Person2(name, age) if age < 18 => s"$name is a minor"
    case Person2(name, age) => s"$name is $age years old"

  println(describe(alice))
  println(describe(bob))
  println(describe(Person2("Charlie", 15)))

  // Nested case classes and deep copy
  println("\n=== Nested Case Classes ===")
  val emp = Employee(
    "Alice",
    "Engineer",
    Address("123 Main St", "Springfield", "US")
  )
  println(s"Employee: $emp")
  val relocated = emp.copy(address = emp.address.copy(city = "Portland"))
  println(s"Relocated: $relocated")

  // Generic case class
  println("\n=== Generic Case Class ===")
  val intPair = Pair(1, 2)
  val mixedPair = Pair("hello", 42)
  println(s"Int pair: $intPair")
  println(s"Mixed pair: $mixedPair")

  // Case class with methods
  println("\n=== Case Class with Methods ===")
  val v1 = Vector2D(3.0, 4.0)
  val v2 = Vector2D(1.0, 2.0)
  println(s"v1 = $v1, magnitude = ${v1.magnitude}")
  println(s"v1 + v2 = ${v1 + v2}")
  println(s"v1 * 2 = ${v1 * 2}")
  println(s"v1.normalized = ${v1.normalized}")

  // Destructuring
  println("\n=== Destructuring ===")
  val Person2(name, age) = alice: @unchecked
  println(s"Destructured: name=$name, age=$age")

  val people = List(alice, bob, Person2("Charlie", 35))
  val names = people.map { case Person2(n, _) => n }
  println(s"Names: $names")

  // Case classes in collections
  println("\n=== Collections ===")
  val sorted = people.sortBy(_.age)
  println(s"Sorted by age: $sorted")
  val grouped = people.groupBy(_.age > 28)
  println(s"Over 28: ${grouped(true)}")
  println(s"28 or under: ${grouped(false)}")
