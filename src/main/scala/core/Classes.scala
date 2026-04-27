package core

// Basic class with constructor parameters
class Person(val name: String, val age: Int):
  // Method
  def greet(): String = s"Hi, I'm $name and I'm $age years old"

  // Override toString
  override def toString: String = s"Person($name, $age)"

// Class with private constructor param (not accessible outside)
class Counter(private var count: Int = 0):
  def increment(): Unit = count += 1
  def decrement(): Unit = count -= 1
  def value: Int = count
  override def toString: String = s"Counter($count)"

// Class with auxiliary constructor
class Point(val x: Double, val y: Double):
  def this() = this(0.0, 0.0)
  def distanceTo(other: Point): Double =
    Math.sqrt(Math.pow(x - other.x, 2) + Math.pow(y - other.y, 2))
  override def toString: String = s"Point($x, $y)"

// Inheritance
class Animal(val name: String):
  def speak(): String = s"$name makes a sound"

class Dog(name: String, val breed: String) extends Animal(name):
  override def speak(): String = s"$name says: Woof!"

class Cat(name: String) extends Animal(name):
  override def speak(): String = s"$name says: Meow!"

// Abstract class
abstract class Shape:
  def area: Double
  def perimeter: Double
  def describe: String = s"Shape with area=${"%.2f".format(area)} and perimeter=${"%.2f".format(perimeter)}"

class Circle(val radius: Double) extends Shape:
  def area: Double = Math.PI * radius * radius
  def perimeter: Double = 2 * Math.PI * radius
  override def toString: String = s"Circle(radius=$radius)"

class Rectangle(val width: Double, val height: Double) extends Shape:
  def area: Double = width * height
  def perimeter: Double = 2 * (width + height)
  override def toString: String = s"Rectangle(${width}x$height)"

// Generic class
class Stack[A]:
  private var elements: List[A] = Nil
  def push(x: A): Unit = elements = x :: elements
  def pop(): A =
    val head = elements.head
    elements = elements.tail
    head
  def peek: A = elements.head
  def isEmpty: Boolean = elements.isEmpty
  override def toString: String = s"Stack(${elements.mkString(", ")})"

@main def classesPoc(): Unit =
  println("=== Scala Classes POC ===")

  // Basic class
  val person = Person("Alice", 30)
  println(s"Person: $person")
  println(s"Name: ${person.name}, Age: ${person.age}")
  println(person.greet())

  // Mutable state with encapsulation
  val counter = Counter()
  counter.increment()
  counter.increment()
  counter.increment()
  counter.decrement()
  println(s"\nCounter value: ${counter.value}")

  // Auxiliary constructor
  val origin = Point()
  val point = Point(3.0, 4.0)
  println(s"\nOrigin: $origin")
  println(s"Point: $point")
  println(s"Distance from origin: ${origin.distanceTo(point)}")

  // Inheritance
  println("\n=== Inheritance ===")
  val animals: List[Animal] = List(Dog("Rex", "Labrador"), Cat("Whiskers"), Dog("Buddy", "Poodle"))
  animals.foreach(a => println(a.speak()))

  // Abstract class
  println("\n=== Abstract Classes ===")
  val shapes: List[Shape] = List(Circle(5.0), Rectangle(4.0, 6.0))
  shapes.foreach(s => println(s"$s -> ${s.describe}"))

  // Generic class
  println("\n=== Generic Class ===")
  val intStack = Stack[Int]
  intStack.push(1)
  intStack.push(2)
  intStack.push(3)
  println(s"Stack: $intStack")
  println(s"Pop: ${intStack.pop()}")
  println(s"After pop: $intStack")

  val stringStack = Stack[String]
  stringStack.push("hello")
  stringStack.push("world")
  println(s"String stack: $stringStack")
