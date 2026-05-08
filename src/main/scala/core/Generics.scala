package core

// Generics - parameterized types for type-safe, reusable code
// Covers: type parameters, bounds, variance, context bounds, type classes

@main def genericsPoc(): Unit =

  // === Basic generic class ===
  case class Box[A](value: A):
    def map[B](f: A => B): Box[B] = Box(f(value))
    def flatMap[B](f: A => Box[B]): Box[B] = f(value)

  println("=== Basic Generic Class ===")
  val intBox = Box(42)
  val stringBox = Box("hello")
  val mappedBox = intBox.map(_ * 2)
  println(s"intBox: $intBox")
  println(s"stringBox: $stringBox")
  println(s"mapped: $mappedBox")

  // === Generic functions ===
  def identity[A](a: A): A = a
  def pair[A, B](a: A, b: B): (A, B) = (a, b)
  def swap[A, B](t: (A, B)): (B, A) = (t._2, t._1)

  println("\n=== Generic Functions ===")
  println(s"identity(42) = ${identity(42)}")
  println(s"pair(1, \"a\") = ${pair(1, "a")}")
  println(s"swap((1, \"a\")) = ${swap((1, "a"))}")

  // === Generic data structure: Stack ===
  class Stack[A] private (private val elements: List[A]):
    def push(a: A): Stack[A] = Stack(a :: elements)
    def pop: (A, Stack[A]) = elements match
      case head :: tail => (head, Stack(tail))
      case Nil          => throw new NoSuchElementException("empty stack")
    def peek: A = elements.head
    def isEmpty: Boolean = elements.isEmpty
    def size: Int = elements.length
    override def toString: String = s"Stack(${elements.mkString(", ")})"

  object Stack:
    def empty[A]: Stack[A] = Stack(Nil)
    def apply[A](elements: List[A]): Stack[A] = new Stack(elements)

  println("\n=== Generic Stack ===")
  val stack = Stack.empty[Int].push(1).push(2).push(3)
  println(s"stack: $stack")
  val (top, rest) = stack.pop
  println(s"pop: top=$top, rest=$rest")

  // === Upper bounds (<:) ===
  // A must be a subtype of Comparable
  trait Animal(val name: String):
    override def toString: String = name

  class Dog(n: String, val tricks: Int) extends Animal(n) with Ordered[Dog]:
    def compare(that: Dog): Int = this.tricks - that.tricks

  def max[A <: Ordered[A]](a: A, b: A): A =
    if a > b then a else b

  println("\n=== Upper Bounds ===")
  val rex = Dog("Rex", 5)
  val buddy = Dog("Buddy", 8)
  println(s"max tricks: ${max(rex, buddy)}") // Buddy

  // === Lower bounds (>:) ===
  // Useful for covariant containers
  sealed trait MyList[+A]:
    // B >: A allows adding supertypes
    def prepend[B >: A](elem: B): MyList[B] = Cons(elem, this)

  case class Cons[+A](head: A, tail: MyList[A]) extends MyList[A]:
    override def toString: String = s"$head :: $tail"
  case object Empty extends MyList[Nothing]:
    override def toString: String = "Empty"

  println("\n=== Lower Bounds & Covariance ===")
  val intList: MyList[Int] = Cons(1, Cons(2, Cons(3, Empty)))
  println(s"intList: $intList")
  // Can prepend Any to a List[Int], result is List[Any]
  val anyList = intList.prepend("hello")
  println(s"prepend string: $anyList")

  // === Variance ===
  // +A = covariant (if Dog <: Animal, Box[Dog] <: Box[Animal])
  // -A = contravariant (if Dog <: Animal, Printer[Animal] <: Printer[Dog])
  // A  = invariant (no subtyping relationship)

  // Covariant: Producer of A
  class Cage[+A](val animal: A)

  // Contravariant: Consumer of A
  trait Printer[-A]:
    def print(a: A): String

  println("\n=== Variance ===")
  val dogCage: Cage[Dog] = Cage(rex)
  val animalCage: Cage[Animal] = dogCage // OK: covariant
  println(s"animalCage contains: ${animalCage.animal}")

  val animalPrinter: Printer[Animal] = new Printer[Animal]:
    def print(a: Animal): String = s"Animal: ${a.name}"

  val dogPrinter: Printer[Dog] = animalPrinter // OK: contravariant
  println(dogPrinter.print(rex))

  // === Context bounds & type classes ===
  // [A: Ordering] means "there exists an implicit Ordering[A]"
  trait JsonEncoder[A]:
    def encode(a: A): String

  object JsonEncoder:
    given JsonEncoder[Int] with
      def encode(a: Int): String = a.toString
    given JsonEncoder[String] with
      def encode(a: String): String = s"\"$a\""
    given listEncoder[A](using enc: JsonEncoder[A]): JsonEncoder[List[A]] with
      def encode(a: List[A]): String = a.map(enc.encode).mkString("[", ",", "]")

  def toJson[A: JsonEncoder](a: A): String =
    summon[JsonEncoder[A]].encode(a)

  println("\n=== Context Bounds & Type Classes ===")
  println(s"toJson(42) = ${toJson(42)}")
  println(s"toJson(\"hi\") = ${toJson("hi")}")
  println(s"toJson(List(1,2,3)) = ${toJson(List(1, 2, 3))}")

  // === Generic extension methods ===
  extension [A](a: A)
    def boxed: Box[A] = Box(a)
    def paired(b: A): (A, A) = (a, b)

  println("\n=== Generic Extensions ===")
  println(s"42.boxed = ${42.boxed}")
  println(s"\"hi\".paired(\"lo\") = ${"hi".paired("lo")}")

  // === Multiple type parameters with constraints ===
  def transform[A, B](items: List[A])(f: A => B): List[B] =
    items.map(f)

  def zipWith[A, B, C](as: List[A], bs: List[B])(f: (A, B) => C): List[C] =
    as.zip(bs).map(f.tupled)

  println("\n=== Multiple Type Parameters ===")
  println(s"transform: ${transform(List(1, 2, 3))(_.toString + "!")}")
  println(s"zipWith: ${zipWith(List(1, 2, 3), List("a", "b", "c"))((n, s) => s"$s$n")}")
