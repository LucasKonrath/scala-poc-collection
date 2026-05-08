package core

// List - immutable singly-linked list, Scala's fundamental collection
// Covers: creation, head/tail, pattern matching, HOFs, recursion, performance

@main def listPoc(): Unit =

  // === Creating lists ===
  val nums = List(1, 2, 3, 4, 5)
  val empty = List.empty[Int]
  val filled = List.fill(5)("x")
  val tabulated = List.tabulate(5)(n => n * n) // 0, 1, 4, 9, 16
  val ranged = List.range(1, 10, 2) // 1, 3, 5, 7, 9

  println("=== Creating Lists ===")
  println(s"nums: $nums")
  println(s"empty: $empty")
  println(s"filled: $filled")
  println(s"tabulated: $tabulated")
  println(s"ranged: $ranged")

  // === Cons operator (::) ===
  // Lists are built right-to-left with ::
  val built = 1 :: 2 :: 3 :: Nil
  val prepended = 0 :: nums

  println("\n=== Cons Operator ===")
  println(s"1 :: 2 :: 3 :: Nil = $built")
  println(s"0 :: nums = $prepended")

  // === Head, tail, and basic access ===
  println("\n=== Access ===")
  println(s"head: ${nums.head}")         // 1
  println(s"tail: ${nums.tail}")         // List(2,3,4,5)
  println(s"last: ${nums.last}")         // 5
  println(s"init: ${nums.init}")         // List(1,2,3,4)
  println(s"nums(2): ${nums(2)}")        // 3 (O(n) access!)
  println(s"take(3): ${nums.take(3)}")   // List(1,2,3)
  println(s"drop(3): ${nums.drop(3)}")   // List(4,5)

  // === Pattern matching on lists ===
  def describe(lst: List[Int]): String = lst match
    case Nil                => "empty"
    case x :: Nil           => s"singleton: $x"
    case x :: y :: Nil      => s"pair: $x, $y"
    case x :: y :: rest     => s"starts with $x, $y then ${rest.length} more"

  println("\n=== Pattern Matching ===")
  println(describe(Nil))
  println(describe(List(1)))
  println(describe(List(1, 2)))
  println(describe(List(1, 2, 3, 4, 5)))

  // === Higher-order functions ===
  println("\n=== Map, Filter, Reduce ===")
  println(s"map (*2): ${nums.map(_ * 2)}")
  println(s"filter (even): ${nums.filter(_ % 2 == 0)}")
  println(s"reduce (+): ${nums.reduce(_ + _)}")
  println(s"fold (sum from 100): ${nums.foldLeft(100)(_ + _)}")

  // === FlatMap ===
  val nested = List(List(1, 2), List(3, 4), List(5))
  println("\n=== FlatMap ===")
  println(s"flatten: ${nested.flatten}")
  println(s"flatMap: ${nums.flatMap(n => List(n, n * 10))}")

  // === Collect (partial function) ===
  val mixed: List[Any] = List(1, "two", 3, "four", 5)
  val onlyInts = mixed.collect { case n: Int => n }
  println(s"\ncollect ints: $onlyInts")

  // === Grouping and partitioning ===
  println("\n=== Grouping ===")
  val (evens, odds) = nums.partition(_ % 2 == 0)
  println(s"evens: $evens, odds: $odds")

  val grouped = List("apple", "banana", "avocado", "cherry", "blueberry")
    .groupBy(_.head)
  println(s"grouped by first letter: $grouped")

  // === Sorting ===
  val unsorted = List(3, 1, 4, 1, 5, 9, 2, 6)
  println("\n=== Sorting ===")
  println(s"sorted: ${unsorted.sorted}")
  println(s"sortBy length: ${List("cat", "a", "elephant", "be").sortBy(_.length)}")
  println(s"sortWith: ${unsorted.sortWith(_ > _)}") // descending

  // === Folding (left and right) ===
  println("\n=== Folding ===")
  // foldLeft: ((((0 + 1) + 2) + 3) + 4) + 5
  val sum = nums.foldLeft(0)(_ + _)
  println(s"foldLeft sum: $sum")

  // foldRight builds from right
  val asString = nums.foldRight("end")((n, acc) => s"$n -> $acc")
  println(s"foldRight: $asString")

  // scan shows intermediate results
  val running = nums.scanLeft(0)(_ + _)
  println(s"scanLeft (running sum): $running")

  // === Zipping ===
  val letters = List("a", "b", "c")
  println("\n=== Zipping ===")
  println(s"zip: ${nums.zip(letters)}")
  println(s"zipWithIndex: ${letters.zipWithIndex}")
  println(s"unzip: ${nums.zip(letters).unzip}")

  // === List concatenation ===
  val a = List(1, 2, 3)
  val b = List(4, 5, 6)
  println("\n=== Concatenation ===")
  println(s"a ++ b: ${a ++ b}")
  println(s"a ::: b: ${a ::: b}") // List-specific concat

  // === Useful operations ===
  println("\n=== Useful Operations ===")
  println(s"distinct: ${List(1,2,2,3,3,3).distinct}")
  println(s"sliding(3): ${nums.sliding(3).toList}")
  println(s"grouped(2): ${nums.grouped(2).toList}")
  println(s"mkString: ${nums.mkString("[", ", ", "]")}")
  println(s"exists (>3): ${nums.exists(_ > 3)}")
  println(s"forall (>0): ${nums.forall(_ > 0)}")
  println(s"count (even): ${nums.count(_ % 2 == 0)}")
  println(s"find (>3): ${nums.find(_ > 3)}")

  // === Recursive list processing ===
  def mySum(lst: List[Int]): Int = lst match
    case Nil => 0
    case head :: tail => head + mySum(tail)

  // Tail-recursive version
  @scala.annotation.tailrec
  def myReverse[A](lst: List[A], acc: List[A] = Nil): List[A] = lst match
    case Nil => acc
    case head :: tail => myReverse(tail, head :: acc)

  println("\n=== Recursive Processing ===")
  println(s"mySum: ${mySum(List(1, 2, 3, 4, 5))}")
  println(s"myReverse: ${myReverse(List(1, 2, 3, 4, 5))}")

  // === Performance note ===
  println("\n=== Performance Notes ===")
  println("List is a singly-linked list:")
  println("  O(1): head, tail, prepend (::)")
  println("  O(n): apply(i), length, append, last")
  println("  Use Vector for random access, ArrayBuffer for mutations")
