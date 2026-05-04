package core

// Sealed trait - all subtypes must be defined in the same file
// Enables exhaustive pattern matching (compiler warns if cases are missing)

// Classic ADT: Option-like type
sealed trait Maybe[+A]
case class Just[+A](value: A) extends Maybe[A]
case object Nada extends Maybe[Nothing]

// Sealed trait as enum (before Scala 3 enum keyword)
sealed trait Season
case object Spring extends Season
case object Summer extends Season
case object Autumn extends Season
case object Winter extends Season

// Sealed trait with data variants (Expression tree)
sealed trait Expr
case class Num(value: Double) extends Expr
case class Add(left: Expr, right: Expr) extends Expr
case class Mul(left: Expr, right: Expr) extends Expr
case class Neg(expr: Expr) extends Expr
case class Var(name: String) extends Expr

// Sealed trait hierarchy (AST for a simple language)
sealed trait Statement
case class Let(name: String, value: Expr) extends Statement
case class Print(expr: Expr) extends Statement
case class Block(statements: List[Statement]) extends Statement

// Sealed trait for error handling (Either-like)
sealed trait Result[+E, +A]
case class Success[+A](value: A) extends Result[Nothing, A]
case class Failure[+E](error: E) extends Result[E, Nothing]

// Sealed trait with shared behavior
sealed trait Shape3:
  def area: Double

case class Circle3(radius: Double) extends Shape3:
  def area: Double = Math.PI * radius * radius

case class Square(side: Double) extends Shape3:
  def area: Double = side * side

case class Triangle(base: Double, height: Double) extends Shape3:
  def area: Double = 0.5 * base * height

// Nested sealed traits
sealed trait Animal2
sealed trait DomesticAnimal extends Animal2
sealed trait WildAnimal extends Animal2

case class DogBreed(name: String, breed: String) extends DomesticAnimal
case class CatBreed(name: String, breed: String) extends DomesticAnimal
case class Wolf(name: String) extends WildAnimal
case class Bear(name: String, species: String) extends WildAnimal

// Recursive sealed trait (JSON-like structure)
sealed trait Json
case object JNull extends Json
case class JBool(value: Boolean) extends Json
case class JNum(value: Double) extends Json
case class JStr(value: String) extends Json
case class JArr(items: List[Json]) extends Json
case class JObj(fields: Map[String, Json]) extends Json

@main def sealedTraitsPoc(): Unit =
  println("=== Scala Sealed Traits POC ===")

  // Maybe type - exhaustive matching
  def showMaybe[A](m: Maybe[A]): String = m match
    case Just(value) => s"Just($value)"
    case Nada        => "Nada"

  val found: Maybe[Int] = Just(42)
  val notFound: Maybe[Int] = Nada
  println(s"found: ${showMaybe(found)}")
  println(s"notFound: ${showMaybe(notFound)}")

  // Season - compiler ensures all cases covered
  println("\n=== Seasons ===")
  def weather(season: Season): String = season match
    case Spring => "Mild and rainy"
    case Summer => "Hot and sunny"
    case Autumn => "Cool and windy"
    case Winter => "Cold and snowy"
  // Removing any case above would cause a compiler warning!

  val seasons = List(Spring, Summer, Autumn, Winter)
  seasons.foreach(s => println(s"  $s -> ${weather(s)}"))

  // Expression tree evaluation
  println("\n=== Expression Tree ===")
  def eval(expr: Expr, env: Map[String, Double] = Map.empty): Double = expr match
    case Num(v)      => v
    case Add(l, r)   => eval(l, env) + eval(r, env)
    case Mul(l, r)   => eval(l, env) * eval(r, env)
    case Neg(e)      => -eval(e, env)
    case Var(name)   => env.getOrElse(name, throw new RuntimeException(s"Undefined: $name"))

  def prettyPrint(expr: Expr): String = expr match
    case Num(v)      => v.toString
    case Add(l, r)   => s"(${prettyPrint(l)} + ${prettyPrint(r)})"
    case Mul(l, r)   => s"(${prettyPrint(l)} * ${prettyPrint(r)})"
    case Neg(e)      => s"(-${prettyPrint(e)})"
    case Var(name)   => name

  // (2 + 3) * x where x = 4
  val expr = Mul(Add(Num(2), Num(3)), Var("x"))
  val env = Map("x" -> 4.0)
  println(s"Expression: ${prettyPrint(expr)}")
  println(s"With x=4: ${eval(expr, env)}")

  // Result type for error handling
  println("\n=== Result Type ===")
  def divide(a: Double, b: Double): Result[String, Double] =
    if b == 0 then Failure("Division by zero")
    else Success(a / b)

  def showResult[E, A](r: Result[E, A]): String = r match
    case Success(value) => s"Success: $value"
    case Failure(error) => s"Failure: $error"

  println(showResult(divide(10, 3)))
  println(showResult(divide(10, 0)))

  // Shapes with exhaustive matching
  println("\n=== Shapes ===")
  def describeShape(s: Shape3): String = s match
    case Circle3(r)       => f"Circle with radius $r, area = ${s.area}%.2f"
    case Square(side)     => f"Square with side $side, area = ${s.area}%.2f"
    case Triangle(b, h)   => f"Triangle with base $b and height $h, area = ${s.area}%.2f"

  val shapes: List[Shape3] = List(Circle3(5), Square(4), Triangle(6, 3))
  shapes.foreach(s => println(s"  ${describeShape(s)}"))

  // Nested sealed traits
  println("\n=== Nested Sealed Traits ===")
  def describeAnimal(a: Animal2): String = a match
    case DogBreed(name, breed)    => s"Domestic dog: $name ($breed)"
    case CatBreed(name, breed)    => s"Domestic cat: $name ($breed)"
    case Wolf(name)               => s"Wild wolf: $name"
    case Bear(name, species)      => s"Wild $species bear: $name"

  val animals: List[Animal2] = List(
    DogBreed("Rex", "Labrador"),
    CatBreed("Whiskers", "Siamese"),
    Wolf("Ghost"),
    Bear("Baloo", "Grizzly")
  )
  animals.foreach(a => println(s"  ${describeAnimal(a)}"))

  // JSON-like recursive structure
  println("\n=== JSON-like Recursive Structure ===")
  def jsonToString(json: Json, indent: Int = 0): String =
    val pad = "  " * indent
    json match
      case JNull      => "null"
      case JBool(v)   => v.toString
      case JNum(v)    => v.toString
      case JStr(v)    => s"\"$v\""
      case JArr(items) =>
        val inner = items.map(i => s"$pad  ${jsonToString(i, indent + 1)}").mkString(",\n")
        s"[\n$inner\n$pad]"
      case JObj(fields) =>
        val inner = fields.map { (k, v) =>
          s"$pad  \"$k\": ${jsonToString(v, indent + 1)}"
        }.mkString(",\n")
        s"{\n$inner\n$pad}"

  val json = JObj(Map(
    "name" -> JStr("Alice"),
    "age" -> JNum(30),
    "active" -> JBool(true),
    "address" -> JNull,
    "scores" -> JArr(List(JNum(95), JNum(87), JNum(92)))
  ))
  println(jsonToString(json))
