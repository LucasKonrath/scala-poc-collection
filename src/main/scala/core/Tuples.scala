package core

// Tuples - fixed-size heterogeneous collections
// Unlike arrays/lists, each element can have a different type
// Scala 3 supports tuples of any arity (not limited to 22)

@main def tuplesPoc(): Unit =

  // === Basic tuples ===
  val pair: (Int, String) = (1, "one")
  val triple: (Int, String, Boolean) = (42, "answer", true)
  val quad = (1, "two", 3.0, '4')

  println("=== Basic Tuples ===")
  println(s"pair: $pair")
  println(s"triple: $triple")
  println(s"quad: $quad")

  // === Accessing elements (1-based with _N) ===
  println("\n=== Element Access ===")
  println(s"pair._1 = ${pair._1}")   // 1
  println(s"pair._2 = ${pair._2}")   // one
  println(s"triple._3 = ${triple._3}") // true

  // === Destructuring ===
  val (num, word) = pair
  val (id, name, active) = triple

  println("\n=== Destructuring ===")
  println(s"num=$num, word=$word")
  println(s"id=$id, name=$name, active=$active")

  // Ignoring elements with _
  val (first, _, _, last) = quad
  println(s"first=$first, last=$last")

  // === Tuples as return values (multiple returns) ===
  def divmod(a: Int, b: Int): (Int, Int) =
    (a / b, a % b)

  def minMax(xs: List[Int]): (Int, Int) =
    (xs.min, xs.max)

  def stats(xs: List[Double]): (Double, Double, Double) =
    val sum = xs.sum
    val mean = sum / xs.size
    val variance = xs.map(x => (x - mean) * (x - mean)).sum / xs.size
    (xs.min, mean, xs.max)

  println("\n=== Multiple Return Values ===")
  val (quotient, remainder) = divmod(17, 5)
  println(s"17 / 5 = $quotient remainder $remainder")

  val (lo, hi) = minMax(List(3, 1, 4, 1, 5, 9))
  println(s"min=$lo, max=$hi")

  val (minVal, meanVal, maxVal) = stats(List(1.0, 2.0, 3.0, 4.0, 5.0))
  println(s"min=$minVal, mean=$meanVal, max=$maxVal")

  // === Tuples in collections ===
  val inventory = List(
    ("apple", 3, 1.50),
    ("banana", 5, 0.75),
    ("cherry", 20, 0.25)
  )

  println("\n=== Tuples in Collections ===")
  for (fruit, qty, price) <- inventory do
    println(f"$fruit%-8s qty=$qty%2d price=$$${qty * price}%.2f")

  // === Map entries are tuples ===
  val scores = Map("Alice" -> 95, "Bob" -> 87, "Carol" -> 92)
  // -> is syntactic sugar for creating a Tuple2

  println("\n=== Map Entries as Tuples ===")
  for (name, score) <- scores do
    println(s"$name: $score")

  println(s"\"Alice\" -> 95 is the same as (\"Alice\", 95): ${("Alice" -> 95) == ("Alice", 95)}")

  // === Tuple operations ===
  println("\n=== Tuple Operations ===")

  // Swap (only for Tuple2)
  val swapped = pair.swap
  println(s"$pair swapped = $swapped") // (one, 1)

  // toList (only for homogeneous tuples conceptually, but works via productIterator)
  val homogeneous = (1, 2, 3)
  println(s"$homogeneous as list = ${homogeneous.toList}")

  // === Zip creates tuples ===
  val names = List("Alice", "Bob", "Carol")
  val ages = List(30, 25, 35)
  val zipped: List[(String, Int)] = names.zip(ages)

  println("\n=== Zip Creates Tuples ===")
  println(s"zipped: $zipped")

  // zipWithIndex
  val indexed = names.zipWithIndex
  println(s"indexed: $indexed")

  // unzip separates tuples
  val (unzippedNames, unzippedAges) = zipped.unzip
  println(s"unzipped names: $unzippedNames")
  println(s"unzipped ages: $unzippedAges")

  // === Pattern matching with tuples ===
  def describePoint(point: (Int, Int)): String = point match
    case (0, 0) => "origin"
    case (x, 0) => s"on x-axis at $x"
    case (0, y) => s"on y-axis at $y"
    case (x, y) if x == y => s"on diagonal at $x"
    case (x, y) => s"point ($x, $y)"

  println("\n=== Pattern Matching with Tuples ===")
  List((0,0), (3,0), (0,5), (4,4), (2,7)).foreach: p =>
    println(s"$p -> ${describePoint(p)}")

  // === Named tuples (Scala 3.x) ===
  // Simulated with case classes for clarity
  case class Coordinate(x: Double, y: Double)
  case class Range(min: Int, max: Int)

  val coord = Coordinate(3.0, 4.0)
  val range = Range(1, 100)
  println("\n=== Named Tuples (via case classes) ===")
  println(s"coord.x=${coord.x}, coord.y=${coord.y}")
  println(s"range.min=${range.min}, range.max=${range.max}")

  // === Nested tuples ===
  val nested: ((Int, Int), (String, String)) = ((1, 2), ("a", "b"))
  val ((a, b), (c, d)) = nested

  println("\n=== Nested Tuples ===")
  println(s"a=$a, b=$b, c=$c, d=$d")

  // === Practical: grouping and partitioning ===
  val numbers = List(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
  val (evens, odds) = numbers.partition(_ % 2 == 0)

  println("\n=== Partition Returns Tuple ===")
  println(s"evens: $evens")
  println(s"odds: $odds")

  // splitAt also returns a tuple
  val (firstHalf, secondHalf) = numbers.splitAt(5)
  println(s"first half: $firstHalf")
  println(s"second half: $secondHalf")
