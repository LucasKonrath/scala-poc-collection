package core

// Pattern Matching - Scala's most powerful control structure
// Goes far beyond switch/case: destructuring, guards, type checks, and custom extractors

@main def patternMatchingPoc(): Unit =

  // === Basic value matching ===
  def describe(x: Any): String = x match
    case 0          => "zero"
    case 1          => "one"
    case "hello"    => "a greeting"
    case true       => "truth"
    case _          => "something else"

  println("=== Basic Value Matching ===")
  println(describe(0))       // zero
  println(describe("hello")) // a greeting
  println(describe(42))      // something else

  // === Type matching ===
  def typeOf(x: Any): String = x match
    case _: Int    => "integer"
    case _: String => "string"
    case _: Double => "double"
    case _: List[?] => "list"
    case _         => "unknown"

  println("\n=== Type Matching ===")
  println(typeOf(42))        // integer
  println(typeOf("hi"))      // string
  println(typeOf(List(1,2))) // list

  // === Pattern matching with guards ===
  def classify(n: Int): String = n match
    case x if x < 0  => "negative"
    case 0            => "zero"
    case x if x % 2 == 0 => s"positive even ($x)"
    case x            => s"positive odd ($x)"

  println("\n=== Guards ===")
  println(classify(-5))  // negative
  println(classify(0))   // zero
  println(classify(4))   // positive even (4)
  println(classify(7))   // positive odd (7)

  // === Destructuring case classes ===
  case class Person(name: String, age: Int)
  case class Address(city: String, country: String)
  case class Contact(person: Person, address: Address)

  val contact = Contact(Person("Alice", 30), Address("Berlin", "Germany"))

  val greeting = contact match
    case Contact(Person(name, age), Address(city, _)) if age >= 18 =>
      s"$name ($age) from $city - adult"
    case Contact(Person(name, _, ), _) =>
      s"$name - minor"

  println("\n=== Destructuring ===")
  println(greeting) // Alice (30) from Berlin - adult

  // === Matching collections ===
  def describeList(lst: List[Int]): String = lst match
    case Nil              => "empty"
    case head :: Nil      => s"single element: $head"
    case head :: tail     => s"head=$head, tail=$tail"

  println("\n=== Collection Matching ===")
  println(describeList(Nil))         // empty
  println(describeList(List(1)))     // single element: 1
  println(describeList(List(1,2,3))) // head=1, tail=List(2, 3)

  // === Tuple matching ===
  def describePair(pair: (Any, Any)): String = pair match
    case (0, 0)          => "origin"
    case (x: Int, 0)     => s"x-axis at $x"
    case (0, y: Int)     => s"y-axis at $y"
    case (a, b)          => s"point ($a, $b)"

  println("\n=== Tuple Matching ===")
  println(describePair((0, 0)))   // origin
  println(describePair((3, 0)))   // x-axis at 3
  println(describePair((1, 2)))   // point (1, 2)

  // === Sealed trait exhaustive matching ===
  enum Color:
    case Red, Green, Blue

  def toHex(c: Color): String = c match
    case Color.Red   => "#FF0000"
    case Color.Green => "#00FF00"
    case Color.Blue  => "#0000FF"
    // Compiler warns if we miss a case!

  println("\n=== Exhaustive Matching ===")
  println(toHex(Color.Red))   // #FF0000
  println(toHex(Color.Blue))  // #0000FF

  // === Pattern matching in val bindings ===
  val (first, second, third) = (1, "two", 3.0)
  val Person(name, age) = Person("Bob", 25): @unchecked

  println("\n=== Val Binding Patterns ===")
  println(s"first=$first, second=$second, third=$third")
  println(s"name=$name, age=$age")

  // === Pattern matching in for comprehensions ===
  val pairs = List((1, "one"), (2, "two"), (3, "three"))
  val extracted = for (num, word) <- pairs yield s"$num=$word"

  println("\n=== For Comprehension Patterns ===")
  println(extracted) // List(1=one, 2=two, 3=three)

  // === Custom extractor (unapply) ===
  object Even:
    def unapply(n: Int): Option[Int] =
      if n % 2 == 0 then Some(n / 2) else None

  object Email:
    def unapply(s: String): Option[(String, String)] =
      s.split("@") match
        case Array(user, domain) => Some((user, domain))
        case _ => None

  println("\n=== Custom Extractors ===")
  42 match
    case Even(half) => println(s"42 is even, half is $half")

  "alice@example.com" match
    case Email(user, domain) => println(s"user=$user, domain=$domain")

  // === Recursive pattern matching (expression evaluator) ===
  enum MathExpr:
    case Lit(value: Double)
    case Plus(a: MathExpr, b: MathExpr)
    case Times(a: MathExpr, b: MathExpr)

  import MathExpr.*

  def eval(e: MathExpr): Double = e match
    case Lit(v)        => v
    case Plus(a, b)    => eval(a) + eval(b)
    case Times(a, b)   => eval(a) * eval(b)

  // (2 + 3) * 4 = 20
  val expr = Times(Plus(Lit(2), Lit(3)), Lit(4))

  println("\n=== Recursive Pattern Matching ===")
  println(s"(2 + 3) * 4 = ${eval(expr)}") // 20.0

  // === Partial functions (pattern matching as functions) ===
  val divide: PartialFunction[Int, String] =
    case 0 => "can't divide by zero"
    case n => s"100 / $n = ${100 / n}"

  println("\n=== Partial Functions ===")
  println(divide(0))   // can't divide by zero
  println(divide(5))   // 100 / 5 = 20
  println(divide.isDefinedAt(0)) // true

  // Using collect (applies partial function, skips non-matching)
  val data = List(1, "two", 3, "four", 5)
  val onlyInts = data.collect { case n: Int => n * 10 }
  println(s"collect ints: $onlyInts") // List(10, 30, 50)
