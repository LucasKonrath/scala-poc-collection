package core

// Map - immutable key-value collection
// Covers: creation, access, transformation, grouping, default values, merging

@main def mapPoc(): Unit =

  // === Creating maps ===
  val ages = Map("Alice" -> 30, "Bob" -> 25, "Carol" -> 35)
  val empty = Map.empty[String, Int]
  val fromPairs = Map(("x", 1), ("y", 2), ("z", 3))
  val fromList = List("apple", "banana", "cherry").map(s => s -> s.length).toMap

  println("=== Creating Maps ===")
  println(s"ages: $ages")
  println(s"fromPairs: $fromPairs")
  println(s"fromList: $fromList")

  // === Accessing values ===
  println("\n=== Accessing Values ===")
  println(s"ages(\"Alice\"): ${ages("Alice")}")         // 30 (throws if missing!)
  println(s"ages.get(\"Alice\"): ${ages.get("Alice")}")  // Some(30)
  println(s"ages.get(\"Dave\"): ${ages.get("Dave")}")    // None
  println(s"getOrElse: ${ages.getOrElse("Dave", 0)}")    // 0
  println(s"contains: ${ages.contains("Bob")}")          // true

  // === Adding and removing ===
  val updated = ages + ("Dave" -> 28)
  val multiAdd = ages ++ Map("Dave" -> 28, "Eve" -> 22)
  val removed = ages - "Bob"
  val multiRemove = ages -- List("Bob", "Carol")

  println("\n=== Adding & Removing ===")
  println(s"+ Dave: $updated")
  println(s"++ multi: $multiAdd")
  println(s"- Bob: $removed")
  println(s"-- multi: $multiRemove")

  // === Updating values ===
  val updatedAge = ages.updated("Alice", 31)
  val transformed = ages.map((k, v) => k.toUpperCase -> (v + 1))

  println("\n=== Updating ===")
  println(s"updated Alice: $updatedAge")
  println(s"transformed: $transformed")

  // === Iterating ===
  println("\n=== Iterating ===")
  ages.foreach((name, age) => println(s"  $name is $age"))

  println("keys: " + ages.keys.toList)
  println("values: " + ages.values.toList)

  // === Higher-order functions ===
  println("\n=== Map, Filter, Collect ===")
  println(s"filter (age>28): ${ages.filter((_, v) => v > 28)}")
  println(s"filterKeys: ${ages.view.filterKeys(_.startsWith("A")).toMap}")
  println(s"mapValues (*2): ${ages.view.mapValues(_ * 2).toMap}")

  val collect = ages.collect:
    case (name, age) if age >= 30 => name -> "senior"
  println(s"collect: $collect")

  // === Folding maps ===
  val totalAge = ages.foldLeft(0)((acc, entry) => acc + entry._2)
  val summary = ages.foldLeft(""): (acc, entry) =>
    val (name, age) = entry
    if acc.isEmpty then s"$name:$age" else s"$acc, $name:$age"

  println("\n=== Folding ===")
  println(s"total age: $totalAge")
  println(s"summary: $summary")

  // === GroupBy (creates maps from collections) ===
  val words = List("apple", "banana", "avocado", "cherry", "blueberry", "apricot")
  val byFirstLetter = words.groupBy(_.head)
  val byLength = words.groupBy(_.length)

  println("\n=== GroupBy ===")
  byFirstLetter.foreach((letter, ws) => println(s"  '$letter': $ws"))
  println(s"by length: $byLength")

  // groupMapReduce: group + map + reduce in one pass
  val wordLengths = words.groupMapReduce(_.head)(_.length)(_ + _)
  println(s"total length by first letter: $wordLengths")

  // === Default values ===
  val withDefault = ages.withDefaultValue(0)
  println("\n=== Default Values ===")
  println(s"withDefault(\"Unknown\"): ${withDefault("Unknown")}") // 0

  val counter = Map.empty[String, Int].withDefaultValue(0)
  val counted = words.foldLeft(counter): (m, w) =>
    val key = w.head.toString
    m.updated(key, m(key) + 1)
  println(s"word count by letter: $counted")

  // === Merging maps ===
  val map1 = Map("a" -> 1, "b" -> 2, "c" -> 3)
  val map2 = Map("b" -> 20, "c" -> 30, "d" -> 40)

  // Simple merge (right wins on conflict)
  val merged = map1 ++ map2
  // Custom merge with foldLeft
  val sumMerged = map2.foldLeft(map1): (acc, entry) =>
    val (k, v) = entry
    acc.updated(k, acc.getOrElse(k, 0) + v)

  println("\n=== Merging ===")
  println(s"simple merge: $merged")
  println(s"sum merge: $sumMerged")

  // === Nested maps ===
  val config = Map(
    "database" -> Map("host" -> "localhost", "port" -> "5432"),
    "cache" -> Map("host" -> "redis", "port" -> "6379")
  )

  println("\n=== Nested Maps ===")
  val dbHost = config.get("database").flatMap(_.get("host"))
  println(s"db host: $dbHost") // Some(localhost)
  val missing = config.get("auth").flatMap(_.get("host"))
  println(s"auth host: $missing") // None

  // === Map to other collections ===
  println("\n=== Conversions ===")
  println(s"toList: ${ages.toList}")
  println(s"toList sorted: ${ages.toList.sortBy(_._2)}")

  // === Practical: word frequency ===
  val text = "the cat sat on the mat the cat"
  val freq = text.split(" ").toList
    .groupMapReduce(identity)(_ => 1)(_ + _)
    .toList.sortBy(-_._2)

  println("\n=== Word Frequency ===")
  freq.foreach((word, count) => println(f"  $word%-6s $count"))

  // === Sorted and ordered maps ===
  import scala.collection.immutable.SortedMap
  val sorted = SortedMap("banana" -> 2, "apple" -> 1, "cherry" -> 3)
  println("\n=== SortedMap ===")
  println(s"sorted: $sorted") // keys in alphabetical order
