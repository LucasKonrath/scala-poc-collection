package core

import scala.util.{Try, Success, Failure}

// Try - functional exception handling
// Success(value) or Failure(exception)
// Wraps computations that may throw into a safe container

@main def tryPoc(): Unit =

  // === Creating Try ===
  val success: Try[Int] = Try(42 / 1)
  val failure: Try[Int] = Try(42 / 0)
  val explicit = Success(42)
  val explicitFail = Failure(new RuntimeException("boom"))

  println("=== Creating Try ===")
  println(s"Try(42/1): $success")   // Success(42)
  println(s"Try(42/0): $failure")   // Failure(ArithmeticException)
  println(s"Success: $explicit")
  println(s"Failure: $explicitFail")

  // === Basic access ===
  println("\n=== Access ===")
  println(s"isSuccess: ${success.isSuccess}")       // true
  println(s"isFailure: ${failure.isFailure}")       // true
  println(s"getOrElse: ${failure.getOrElse(0)}")    // 0
  println(s"orElse: ${failure.orElse(Success(99))}")// Success(99)

  // === Pattern matching ===
  def describe(t: Try[Int]): String = t match
    case Success(n) => s"got $n"
    case Failure(e) => s"failed: ${e.getMessage}"

  println("\n=== Pattern Matching ===")
  println(describe(success))
  println(describe(failure))

  // === Map, flatMap, filter ===
  println("\n=== Transformations ===")
  println(s"map: ${success.map(_ * 2)}")                       // Success(84)
  println(s"map failure: ${failure.map(_ * 2)}")               // Failure(...)
  println(s"flatMap: ${success.flatMap(n => Try(n + 1))}")     // Success(43)
  println(s"filter pass: ${success.filter(_ > 10)}")           // Success(42)
  println(s"filter fail: ${success.filter(_ > 100)}")          // Failure(NoSuchElementException)

  // === Chaining computations ===
  def parseInt(s: String): Try[Int] = Try(s.toInt)
  def divide(a: Int, b: Int): Try[Double] = Try(a.toDouble / b)

  val result = for
    a <- parseInt("100")
    b <- parseInt("4")
    q <- divide(a, b)
  yield q

  println("\n=== Chaining ===")
  println(s"100 / 4: $result") // Success(25.0)

  val badResult = for
    a <- parseInt("abc")
    b <- parseInt("4")
  yield a + b
  println(s"parse error: $badResult") // Failure(NumberFormatException)

  // === Recover and recoverWith ===
  val recovered = failure.recover:
    case _: ArithmeticException => -1

  val recoveredWith = failure.recoverWith:
    case _: ArithmeticException => Success(0)

  println("\n=== Recovery ===")
  println(s"recover: $recovered")         // Success(-1)
  println(s"recoverWith: $recoveredWith") // Success(0)

  // Selective recovery
  val partial = Try("abc".toInt).recover:
    case _: NumberFormatException => 0
    // ArithmeticException would NOT be recovered
  println(s"selective recover: $partial") // Success(0)

  // === Fold ===
  val formatted = success.fold(
    e => s"Error: ${e.getMessage}",
    v => s"Value: $v"
  )
  println(s"\nfold: $formatted") // Value: 42

  // === Convert to Option/Either ===
  println("\n=== Conversions ===")
  println(s"toOption success: ${success.toOption}")   // Some(42)
  println(s"toOption failure: ${failure.toOption}")    // None
  println(s"toEither success: ${success.toEither}")   // Right(42)
  println(s"toEither failure: ${failure.toEither}")    // Left(ArithmeticException)

  // === Try in collections ===
  val inputs = List("1", "abc", "3", "def", "5")
  val parsed = inputs.map(s => parseInt(s))

  println("\n=== Try in Collections ===")
  println(s"all attempts: $parsed")

  // Collect only successes
  val successes = parsed.collect { case Success(n) => n }
  println(s"successes only: $successes") // List(1, 3, 5)

  // Partition
  val (failures, wins) = parsed.partition(_.isFailure)
  println(s"failures: ${failures.length}, successes: ${wins.length}")

  // === Practical: safe resource handling ===
  def readConfig(path: String): Try[Map[String, String]] = Try:
    // Simulating file read that might fail
    if path == "good.conf" then
      Map("host" -> "localhost", "port" -> "8080")
    else
      throw new java.io.FileNotFoundException(s"$path not found")

  def getPort(config: Map[String, String]): Try[Int] =
    config.get("port") match
      case Some(p) => parseInt(p)
      case None    => Failure(new NoSuchElementException("no port configured"))

  val port = for
    config <- readConfig("good.conf")
    port   <- getPort(config)
  yield port

  val badPort = for
    config <- readConfig("missing.conf")
    port   <- getPort(config)
  yield port

  println("\n=== Practical: Config Loading ===")
  println(s"good config: $port")    // Success(8080)
  println(s"bad config: $badPort")  // Failure(FileNotFoundException)

  // === Transform ===
  val transformed = failure.transform(
    s => Success(s.get * 2),
    e => Success(-1)
  )
  println(s"\ntransform: $transformed") // Success(-1)

  // === Chaining with foreach (side effects) ===
  println("\n=== Side Effects ===")
  success.foreach(n => println(s"  processing: $n"))
  failure.foreach(n => println(s"  this won't print: $n"))

  // === Try vs Either vs Option ===
  println("\n=== Comparison ===")
  println("Option: present/absent, no error detail")
  println("Either: success/typed error, no exception semantics")
  println("Try:    success/exception, integrates with Java exceptions")
  println("Rule:   Use Try at boundaries (IO, parsing), Either for domain errors")
