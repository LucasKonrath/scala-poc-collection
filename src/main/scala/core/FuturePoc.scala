package core

import scala.concurrent.{Future, Await, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.*
import scala.util.{Success, Failure}

// Future - asynchronous computation that will eventually produce a value
// Non-blocking, composable, integrates with for-comprehensions

@main def futurePoc(): Unit =

  // === Creating Futures ===
  val f1: Future[Int] = Future:
    Thread.sleep(100)
    42

  val immediate = Future.successful(99)
  val failed = Future.failed(new RuntimeException("boom"))

  println("=== Creating Futures ===")
  println(s"f1 (pending): $f1")
  println(s"immediate: $immediate")
  println(s"failed: $failed")

  // === Blocking to get result (only for demos!) ===
  val result = Await.result(f1, 5.seconds)
  println(s"f1 result: $result") // 42

  // === Callbacks ===
  println("\n=== Callbacks ===")
  val f2 = Future:
    Thread.sleep(50)
    "hello"

  f2.onComplete:
    case Success(value) => println(s"  callback got: $value")
    case Failure(ex)    => println(s"  callback error: ${ex.getMessage}")

  Thread.sleep(200) // Wait for callback

  // === Map and flatMap ===
  val doubled = f1.map(_ * 2)
  val chained = f1.flatMap(n => Future(n + 8))

  println("\n=== Map & FlatMap ===")
  println(s"doubled: ${Await.result(doubled, 5.seconds)}")  // 84
  println(s"chained: ${Await.result(chained, 5.seconds)}")  // 50

  // === For comprehensions (sequential when dependent) ===
  def fetchUser(id: Int): Future[String] = Future:
    Thread.sleep(50)
    s"User-$id"

  def fetchOrders(user: String): Future[List[String]] = Future:
    Thread.sleep(50)
    List(s"$user-order1", s"$user-order2")

  val userOrders = for
    user   <- fetchUser(1)
    orders <- fetchOrders(user)
  yield (user, orders)

  println("\n=== For Comprehensions ===")
  val (user, orders) = Await.result(userOrders, 5.seconds)
  println(s"user: $user, orders: $orders")

  // === Parallel execution ===
  // Start futures BEFORE the for-comprehension to run in parallel
  println("\n=== Parallel Execution ===")
  val start = System.currentTimeMillis()

  val fa = Future { Thread.sleep(200); "A" }
  val fb = Future { Thread.sleep(200); "B" }
  val fc = Future { Thread.sleep(200); "C" }

  val parallel = for
    a <- fa
    b <- fb
    c <- fc
  yield s"$a-$b-$c"

  val parallelResult = Await.result(parallel, 5.seconds)
  val elapsed = System.currentTimeMillis() - start
  println(s"result: $parallelResult (took ~${elapsed}ms, not 600ms)")

  // === Future.sequence (List[Future] -> Future[List]) ===
  val futures = List(
    Future { Thread.sleep(50); 1 },
    Future { Thread.sleep(50); 2 },
    Future { Thread.sleep(50); 3 }
  )
  val sequenced = Future.sequence(futures)

  println("\n=== Sequence ===")
  println(s"sequenced: ${Await.result(sequenced, 5.seconds)}") // List(1,2,3)

  // === Future.traverse (map + sequence in one step) ===
  val ids = List(1, 2, 3)
  val users = Future.traverse(ids)(id => fetchUser(id))

  println("\n=== Traverse ===")
  println(s"users: ${Await.result(users, 5.seconds)}")

  // === Error handling ===
  val risky = Future:
    throw new RuntimeException("something broke")

  val recovered = risky.recover:
    case _: RuntimeException => "recovered value"

  val recoveredWith = risky.recoverWith:
    case _: RuntimeException => Future.successful("recovered async")

  println("\n=== Error Handling ===")
  println(s"recover: ${Await.result(recovered, 5.seconds)}")
  println(s"recoverWith: ${Await.result(recoveredWith, 5.seconds)}")

  // fallbackTo
  val fallback = risky.fallbackTo(Future.successful("fallback value"))
  println(s"fallbackTo: ${Await.result(fallback, 5.seconds)}")

  // === Transform ===
  val transformed = f1.transform(
    success = _ * 10,
    failure = ex => new RuntimeException(s"wrapped: ${ex.getMessage}")
  )
  println(s"\ntransform: ${Await.result(transformed, 5.seconds)}") // 420

  // === Filter and collect ===
  val filtered = Future.successful(42).filter(_ > 10)
  val collected = Future.successful(42).collect:
    case n if n > 10 => s"big: $n"

  println("\n=== Filter & Collect ===")
  println(s"filter: ${Await.result(filtered, 5.seconds)}")
  println(s"collect: ${Await.result(collected, 5.seconds)}")

  // === Zip ===
  val zipped = Future.successful(1).zip(Future.successful("a"))
  println(s"\nzip: ${Await.result(zipped, 5.seconds)}") // (1, a)

  // === Promise (manually completing a Future) ===
  val promise = Promise[String]()
  val promiseFuture: Future[String] = promise.future

  // Complete from another thread
  Future:
    Thread.sleep(100)
    promise.success("promised value")

  println("\n=== Promise ===")
  println(s"promise result: ${Await.result(promiseFuture, 5.seconds)}")

  // === Future.firstCompletedOf (race) ===
  val slow = Future { Thread.sleep(500); "slow" }
  val fast = Future { Thread.sleep(50); "fast" }
  val winner = Future.firstCompletedOf(List(slow, fast))

  println("\n=== Race ===")
  println(s"winner: ${Await.result(winner, 5.seconds)}") // fast

  // === Practical: retry pattern ===
  var attempts = 0
  def unreliable(): Future[String] = Future:
    attempts += 1
    if attempts < 3 then throw new RuntimeException(s"attempt $attempts failed")
    else "finally worked"

  def retry[A](n: Int)(f: => Future[A]): Future[A] =
    f.recoverWith:
      case _ if n > 1 => retry(n - 1)(f)

  println("\n=== Retry Pattern ===")
  val retried = retry(5)(unreliable())
  println(s"retried: ${Await.result(retried, 5.seconds)}") // finally worked
  println(s"took $attempts attempts")

  // === Convert to Try ===
  // Future already has onComplete with Try, but you can also:
  val asTry = f1.transform(scala.util.Try(_))
  println(s"\nas Try: ${Await.result(asTry, 5.seconds)}") // Success(42)

  println("\n=== Notes ===")
  println("Futures start executing immediately upon creation")
  println("They require an ExecutionContext (thread pool)")
  println("Use Await only in tests/main — never in production async code")
  println("Prefer for-comprehensions for composing dependent futures")
