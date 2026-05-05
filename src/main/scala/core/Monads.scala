package core

// Monads - composable computation contexts
// A monad wraps a value and provides flatMap (bind) for chaining computations
// Laws: left identity, right identity, associativity

@main def monadsPoc(): Unit =

  // === What makes a Monad? ===
  // 1. A type constructor: F[_]
  // 2. unit/pure: A => F[A]  (wraps a value)
  // 3. flatMap/bind: F[A] => (A => F[B]) => F[B]  (chains computations)

  // === Monad trait ===
  trait Monad[F[_]]:
    def pure[A](a: A): F[A]
    def flatMap[A, B](fa: F[A])(f: A => F[B]): F[B]
    // map can be derived from flatMap + pure
    def map[A, B](fa: F[A])(f: A => B): F[B] =
      flatMap(fa)(a => pure(f(a)))

  // === Identity Monad (simplest possible monad) ===
  case class Id[A](value: A)

  given Monad[Id] with
    def pure[A](a: A): Id[A] = Id(a)
    def flatMap[A, B](fa: Id[A])(f: A => Id[B]): Id[B] = f(fa.value)

  println("=== Identity Monad ===")
  val idResult = summon[Monad[Id]].flatMap(Id(10))(x => Id(x + 5))
  println(s"Id(10).flatMap(_ + 5) = $idResult") // Id(15)

  // === Option as Monad ===
  // Option is a monad: Some = pure, flatMap = chain or short-circuit on None
  given Monad[Option] with
    def pure[A](a: A): Option[A] = Some(a)
    def flatMap[A, B](fa: Option[A])(f: A => Option[B]): Option[B] = fa.flatMap(f)

  println("\n=== Option Monad ===")
  def safeDivide(a: Int, b: Int): Option[Int] =
    if b == 0 then None else Some(a / b)

  val optResult = for
    a <- safeDivide(100, 5)
    b <- safeDivide(a, 2)
  yield b
  println(s"100 / 5 / 2 = $optResult") // Some(10)

  val optFail = for
    a <- safeDivide(100, 0)
    b <- safeDivide(a, 2)
  yield b
  println(s"100 / 0 / 2 = $optFail") // None (short-circuits)

  // === List as Monad ===
  // List monad: represents non-deterministic computation
  // flatMap = for each element, produce a list, then flatten all results
  given Monad[List] with
    def pure[A](a: A): List[A] = List(a)
    def flatMap[A, B](fa: List[A])(f: A => List[B]): List[B] = fa.flatMap(f)

  println("\n=== List Monad (Non-determinism) ===")
  // All combinations of suits and ranks
  val cards = for
    suit <- List("♠", "♥", "♦", "♣")
    rank <- List("A", "K", "Q")
  yield s"$rank$suit"
  println(s"cards: $cards")

  // Pythagorean triples (list comprehension as search)
  val triples = for
    a <- (1 to 20).toList
    b <- (a to 20).toList
    c <- (b to 20).toList
    if a * a + b * b == c * c
  yield (a, b, c)
  println(s"pythagorean triples: $triples")

  // === Writer Monad (computation with log) ===
  case class Writer[W, A](value: A, log: W)

  // Writer monad needs a way to combine logs (monoid)
  trait Monoid[A]:
    def empty: A
    def combine(a: A, b: A): A

  given Monoid[List[String]] with
    def empty: List[String] = Nil
    def combine(a: List[String], b: List[String]): List[String] = a ++ b

  extension [A](w: Writer[List[String], A])
    def writerMap[B](f: A => B): Writer[List[String], B] =
      Writer(f(w.value), w.log)
    def writerFlatMap[B](f: A => Writer[List[String], B]): Writer[List[String], B] =
      val result = f(w.value)
      Writer(result.value, w.log ++ result.log)

  def tell(msg: String): Writer[List[String], Unit] =
    Writer((), List(msg))

  def writerPure[A](a: A): Writer[List[String], A] =
    Writer(a, Nil)

  println("\n=== Writer Monad (Logging) ===")
  val computation =
    writerPure(10)
      .writerFlatMap(x => Writer(x + 5, List(s"added 5 to $x")))
      .writerFlatMap(x => Writer(x * 2, List(s"multiplied $x by 2")))
      .writerFlatMap(x => Writer(x, List(s"final result: $x")))

  println(s"result: ${computation.value}")
  println(s"log: ${computation.log.mkString(" -> ")}")

  // === State Monad (computation with mutable state) ===
  case class State[S, A](run: S => (A, S)):
    def map[B](f: A => B): State[S, B] =
      State(s =>
        val (a, s1) = run(s)
        (f(a), s1)
      )
    def flatMap[B](f: A => State[S, B]): State[S, B] =
      State(s =>
        val (a, s1) = run(s)
        f(a).run(s1)
      )

  object State:
    def pure[S, A](a: A): State[S, A] = State(s => (a, s))
    def get[S]: State[S, S] = State(s => (s, s))
    def set[S](s: S): State[S, Unit] = State(_ => ((), s))
    def modify[S](f: S => S): State[S, Unit] = State(s => ((), f(s)))

  println("\n=== State Monad ===")

  // Stack operations using State monad
  type Stack = List[Int]

  def push(n: Int): State[Stack, Unit] =
    State.modify(n :: _)

  def pop: State[Stack, Int] =
    State(s => (s.head, s.tail))

  val stackOps = for
    _   <- push(1)
    _   <- push(2)
    _   <- push(3)
    top <- pop
    _   <- push(top * 10)
    s   <- State.get[Stack]
  yield s

  val (finalStack, _) = stackOps.run(Nil)
  println(s"stack after ops: $finalStack") // List(30, 2, 1)

  // Counter example
  def increment: State[Int, Unit] = State.modify(_ + 1)
  def getCount: State[Int, Int] = State.get

  val counting = for
    _ <- increment
    _ <- increment
    _ <- increment
    c <- getCount
  yield c

  val (count, _) = counting.run(0)
  println(s"count: $count") // 3

  // === Reader Monad (dependency injection) ===
  case class Reader[R, A](run: R => A):
    def map[B](f: A => B): Reader[R, B] =
      Reader(r => f(run(r)))
    def flatMap[B](f: A => Reader[R, B]): Reader[R, B] =
      Reader(r => f(run(r)).run(r))

  object Reader:
    def ask[R]: Reader[R, R] = Reader(identity)
    def pure[R, A](a: A): Reader[R, A] = Reader(_ => a)

  case class Config(dbUrl: String, apiKey: String)

  def getDbUrl: Reader[Config, String] =
    Reader(_.dbUrl)

  def getApiKey: Reader[Config, String] =
    Reader(_.apiKey)

  def buildConnection: Reader[Config, String] = for
    url <- getDbUrl
    key <- getApiKey
  yield s"Connected to $url with key=$key"

  println("\n=== Reader Monad (Dependency Injection) ===")
  val config = Config("postgres://localhost/db", "secret123")
  println(s"connection: ${buildConnection.run(config)}")

  // === Monad Laws ===
  println("\n=== Monad Laws ===")

  // Using Option as our test monad
  val f: Int => Option[Int] = x => Some(x + 1)
  val g: Int => Option[Int] = x => if x > 0 then Some(x * 2) else None

  // Law 1: Left identity - pure(a).flatMap(f) == f(a)
  val leftId1 = Some(5).flatMap(f)
  val leftId2 = f(5)
  println(s"Left identity:  ${leftId1 == leftId2}  ($leftId1 == $leftId2)")

  // Law 2: Right identity - m.flatMap(pure) == m
  val rightId1 = Some(5).flatMap(Some(_))
  val rightId2 = Some(5)
  println(s"Right identity: ${rightId1 == rightId2}  ($rightId1 == $rightId2)")

  // Law 3: Associativity - m.flatMap(f).flatMap(g) == m.flatMap(a => f(a).flatMap(g))
  val assoc1 = Some(5).flatMap(f).flatMap(g)
  val assoc2 = Some(5).flatMap(a => f(a).flatMap(g))
  println(s"Associativity:  ${assoc1 == assoc2}  ($assoc1 == $assoc2)")

  // === Summary ===
  println("\n=== Common Monads Summary ===")
  println("Option:  handles absence (Some/None)")
  println("Either:  handles errors with info (Right/Left)")
  println("Try:     handles exceptions (Success/Failure)")
  println("List:    handles non-determinism (multiple values)")
  println("Future:  handles async computation")
  println("State:   threads mutable state functionally")
  println("Reader:  dependency injection / shared config")
  println("Writer:  accumulates a log alongside computation")
  println("IO:      handles side effects (cats-effect, ZIO)")
