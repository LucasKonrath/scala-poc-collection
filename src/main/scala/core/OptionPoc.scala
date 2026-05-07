package core

// Option - safe alternative to null
// Some(value) when present, None when absent
// Forces explicit handling of missing values at compile time

@main def optionPoc(): Unit =

  // === Creating Options ===
  val some: Option[Int] = Some(42)
  val none: Option[Int] = None
  val fromNullable = Option(null)  // None
  val fromValue = Option("hello")  // Some("hello")

  println("=== Creating Options ===")
  println(s"Some(42): $some")
  println(s"None: $none")
  println(s"Option(null): $fromNullable")
  println(s"Option(\"hello\"): $fromValue")

  // === Safe access ===
  println("\n=== Safe Access ===")
  println(s"getOrElse: ${none.getOrElse(0)}")         // 0
  println(s"orElse: ${none.orElse(Some(99))}")         // Some(99)
  println(s"contains: ${some.contains(42)}")           // true
  println(s"isEmpty: ${none.isEmpty}")                 // true
  println(s"isDefined: ${some.isDefined}")             // true
  println(s"nonEmpty: ${some.nonEmpty}")               // true

  // === Pattern matching ===
  def describe(opt: Option[Int]): String = opt match
    case Some(n) if n > 0 => s"positive: $n"
    case Some(n)          => s"non-positive: $n"
    case None             => "nothing"

  println("\n=== Pattern Matching ===")
  println(describe(Some(5)))    // positive: 5
  println(describe(Some(-3)))   // non-positive: -3
  println(describe(None))       // nothing

  // === Map, flatMap, filter ===
  println("\n=== Transformations ===")
  println(s"map: ${some.map(_ * 2)}")           // Some(84)
  println(s"map None: ${none.map(_ * 2)}")      // None
  println(s"filter pass: ${some.filter(_ > 10)}")   // Some(42)
  println(s"filter fail: ${some.filter(_ > 100)}")  // None
  println(s"flatMap: ${some.flatMap(n => if n > 0 then Some(n.toString) else None)}")

  // === Chaining Options (flatMap) ===
  case class User(name: String, email: Option[String])
  case class Address(user: Option[User])
  case class Company(address: Option[Address])

  val company = Company(
    Some(Address(Some(User("Alice", Some("alice@example.com")))))
  )
  val emptyCompany = Company(None)

  // Without Option: null checks everywhere
  // With Option: elegant chaining
  val email = company.address
    .flatMap(_.user)
    .flatMap(_.email)

  val noEmail = emptyCompany.address
    .flatMap(_.user)
    .flatMap(_.email)

  println("\n=== Chaining ===")
  println(s"email: $email")       // Some(alice@example.com)
  println(s"noEmail: $noEmail")   // None

  // === For comprehensions (syntactic sugar for flatMap/map) ===
  val result = for
    addr  <- company.address
    user  <- addr.user
    email <- user.email
  yield email.toUpperCase

  println("\n=== For Comprehensions ===")
  println(s"for-comp result: $result") // Some(ALICE@EXAMPLE.COM)

  val noResult = for
    addr  <- emptyCompany.address
    user  <- addr.user
    email <- user.email
  yield email.toUpperCase
  println(s"for-comp empty: $noResult") // None

  // === Option as collection ===
  println("\n=== Option as Collection ===")
  println(s"foreach: "); some.foreach(n => println(s"  got $n"))
  println(s"toList Some: ${some.toList}")     // List(42)
  println(s"toList None: ${none.toList}")     // List()
  println(s"exists (>10): ${some.exists(_ > 10)}")  // true
  println(s"forall (>10): ${none.forall(_ > 10)}")  // true (vacuous truth)

  // === Flattening Options in collections ===
  val opts = List(Some(1), None, Some(3), None, Some(5))
  println("\n=== Flatten ===")
  println(s"flatten: ${opts.flatten}")                     // List(1, 3, 5)
  println(s"flatMap: ${opts.flatMap(identity)}")            // List(1, 3, 5)

  // === Practical: safe parsing ===
  def parseInt(s: String): Option[Int] =
    try Some(s.toInt)
    catch case _: NumberFormatException => None

  def parseDouble(s: String): Option[Double] =
    try Some(s.toDouble)
    catch case _: NumberFormatException => None

  println("\n=== Safe Parsing ===")
  println(s"parseInt(\"42\"): ${parseInt("42")}")     // Some(42)
  println(s"parseInt(\"abc\"): ${parseInt("abc")}")   // None

  // Combining parsed values
  val sum = for
    a <- parseInt("10")
    b <- parseInt("20")
  yield a + b
  println(s"sum of parsed: $sum") // Some(30)

  val badSum = for
    a <- parseInt("10")
    b <- parseInt("oops")
  yield a + b
  println(s"bad sum: $badSum") // None

  // === Option.when and Option.unless ===
  println("\n=== Conditional Creation ===")
  println(s"when(true): ${Option.when(5 > 3)("yes")}")      // Some("yes")
  println(s"when(false): ${Option.when(5 < 3)("yes")}")     // None
  println(s"unless(true): ${Option.unless(5 > 3)("yes")}")  // None
  println(s"unless(false): ${Option.unless(5 < 3)("yes")}") // Some("yes")

  // === Fold (catamorphism) ===
  println("\n=== Fold ===")
  val formatted = some.fold("N/A")(n => s"Value: $n")
  val formattedNone = none.fold("N/A")(n => s"Value: $n")
  println(s"fold Some: $formatted")     // Value: 42
  println(s"fold None: $formattedNone") // N/A

  // === Zip ===
  println("\n=== Zip ===")
  println(s"zip both: ${Some(1).zip(Some("a"))}")     // Some((1,a))
  println(s"zip one None: ${Some(1).zip(None)}")       // None
  println(s"zip both None: ${None.zip(None)}")         // None

  // === Practical: lookup chain ===
  def findInCache(key: String): Option[String] = None
  def findInDb(key: String): Option[String] =
    if key == "user:1" then Some("Alice") else None
  def findInRemote(key: String): Option[String] = Some("fallback")

  val value = findInCache("user:1")
    .orElse(findInDb("user:1"))
    .orElse(findInRemote("user:1"))

  println("\n=== Lookup Chain ===")
  println(s"found: $value") // Some(Alice) - from DB
