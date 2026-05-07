package core

// Either - represents a value of one of two types: Left (error) or Right (success)
// Right-biased since Scala 2.12: map/flatMap operate on Right by default

@main def eitherPoc(): Unit =

  // === Creating Either ===
  val right: Either[String, Int] = Right(42)
  val left: Either[String, Int] = Left("error")

  println("=== Creating Either ===")
  println(s"Right(42): $right")
  println(s"Left(\"error\"): $left")

  // === Basic access ===
  println("\n=== Access ===")
  println(s"isRight: ${right.isRight}")             // true
  println(s"isLeft: ${left.isLeft}")                // true
  println(s"getOrElse: ${left.getOrElse(0)}")       // 0
  println(s"contains: ${right.contains(42)}")       // true

  // === Pattern matching ===
  def describe(e: Either[String, Int]): String = e match
    case Right(n) if n > 0 => s"positive: $n"
    case Right(n)          => s"non-positive: $n"
    case Left(err)         => s"error: $err"

  println("\n=== Pattern Matching ===")
  println(describe(Right(5)))
  println(describe(Right(-3)))
  println(describe(Left("oops")))

  // === Map, flatMap (right-biased) ===
  println("\n=== Transformations ===")
  println(s"map Right: ${right.map(_ * 2)}")         // Right(84)
  println(s"map Left: ${left.map(_ * 2)}")           // Left(error) - untouched
  println(s"flatMap: ${right.flatMap(n => Right(n + 1))}")

  // === Chaining with flatMap ===
  def parseInt(s: String): Either[String, Int] =
    try Right(s.toInt)
    catch case _: NumberFormatException => Left(s"'$s' is not a number")

  def divide(a: Int, b: Int): Either[String, Double] =
    if b == 0 then Left("division by zero")
    else Right(a.toDouble / b)

  def sqrt(n: Double): Either[String, Double] =
    if n < 0 then Left(s"cannot sqrt negative: $n")
    else Right(math.sqrt(n))

  println("\n=== Chaining ===")
  val result = for
    a <- parseInt("100")
    b <- parseInt("4")
    q <- divide(a, b)
    r <- sqrt(q)
  yield r

  println(s"100 / 4 then sqrt: $result") // Right(5.0)

  val badResult = for
    a <- parseInt("100")
    b <- parseInt("0")
    q <- divide(a, b)
    r <- sqrt(q)
  yield r
  println(s"with division by zero: $badResult") // Left(division by zero)

  val parseError = for
    a <- parseInt("abc")
    b <- parseInt("4")
  yield a + b
  println(s"parse error: $parseError") // Left('abc' is not a number)

  // === Left map (transform the error side) ===
  case class AppError(code: Int, message: String)

  val enriched = left.left.map(msg => AppError(400, msg))
  println("\n=== Left Map ===")
  println(s"enriched error: $enriched") // Left(AppError(400,error))

  // === Fold (handle both sides) ===
  def render(e: Either[String, Int]): String =
    e.fold(
      err => s"Error: $err",
      value => s"Success: $value"
    )

  println("\n=== Fold ===")
  println(render(Right(42)))     // Success: 42
  println(render(Left("oops"))) // Error: oops

  // === Swap ===
  println("\n=== Swap ===")
  println(s"Right(1).swap: ${Right(1).swap}")   // Left(1)
  println(s"Left(\"x\").swap: ${Left("x").swap}") // Right(x)

  // === Either in collections ===
  val results: List[Either[String, Int]] = List(
    Right(1), Left("bad"), Right(3), Left("worse"), Right(5)
  )

  println("\n=== Either in Collections ===")
  val (lefts, rights) = results.partitionMap(identity)
  println(s"lefts: $lefts")   // List(bad, worse)
  println(s"rights: $rights") // List(1, 3, 5)

  // Sequence: List[Either] -> Either[Error, List] (fail on first error)
  def sequence[E, A](eithers: List[Either[E, A]]): Either[E, List[A]] =
    eithers.foldRight(Right(Nil): Either[E, List[A]]): (elem, acc) =>
      for
        list <- acc
        a    <- elem
      yield a :: list

  println(s"sequence all Right: ${sequence(List(Right(1), Right(2), Right(3)))}")
  println(s"sequence with Left: ${sequence(results)}")

  // === Practical: validation ===
  case class UserForm(name: String, age: String, email: String)

  def validateName(name: String): Either[String, String] =
    if name.trim.isEmpty then Left("name is required")
    else if name.length < 2 then Left("name too short")
    else Right(name.trim)

  def validateAge(age: String): Either[String, Int] =
    parseInt(age).flatMap: n =>
      if n < 0 || n > 150 then Left(s"invalid age: $n")
      else Right(n)

  def validateEmail(email: String): Either[String, String] =
    if email.contains("@") then Right(email)
    else Left(s"invalid email: $email")

  case class ValidUser(name: String, age: Int, email: String)

  def validate(form: UserForm): Either[String, ValidUser] =
    for
      name  <- validateName(form.name)
      age   <- validateAge(form.age)
      email <- validateEmail(form.email)
    yield ValidUser(name, age, email)

  println("\n=== Validation ===")
  println(validate(UserForm("Alice", "30", "alice@example.com")))
  println(validate(UserForm("", "30", "alice@example.com")))
  println(validate(UserForm("Alice", "abc", "alice@example.com")))
  println(validate(UserForm("Alice", "30", "not-an-email")))

  // === Either vs Option ===
  println("\n=== Either vs Option ===")
  println("Option: value present or absent (no error info)")
  println("Either: value or specific error (preserves failure reason)")

  // Convert between them
  val opt: Option[Int] = Some(42)
  val fromOpt: Either[String, Int] = opt.toRight("was empty")
  val backToOpt: Option[Int] = right.toOption
  println(s"Option -> Either: $fromOpt")
  println(s"Either -> Option: $backToOpt")

  // === Merge (when both sides are same type) ===
  val either: Either[String, String] = Right("hello")
  println(s"\nmerge: ${either.merge}") // hello (works for Either[A, A])

  // === orElse ===
  println("\n=== OrElse ===")
  println(s"Right orElse: ${right.orElse(Right(0))}")   // Right(42)
  println(s"Left orElse: ${left.orElse(Right(0))}")     // Right(0)
