package core

@main def variablesPoc(): Unit =
  // val - immutable (like final in Java)
  val name: String = "Scala"
  val version: Int = 3
  val pi: Double = 3.14159
  val isAwesome: Boolean = true

  // Type inference - compiler figures out the type
  val language = "Scala" // String inferred
  val year = 2025       // Int inferred
  val ratio = 0.75      // Double inferred

  // var - mutable (use sparingly in idiomatic Scala)
  var counter: Int = 0
  counter = counter + 1
  counter += 1

  // lazy val - evaluated only when first accessed
  lazy val expensiveComputation: String =
    println("  (computing...)")
    "result of expensive computation"

  // String interpolation
  val greeting = s"Hello, $name $version!"
  val formatted = f"Pi is approximately $pi%.2f"
  val raw = raw"No escape: \n stays as \n"

  // Multi-line strings
  val multiLine =
    """
      |This is a
      |multi-line string
      |with strip margin
    """.stripMargin

  // Printing results
  println("=== Scala Variables POC ===")
  println(s"val name: $name")
  println(s"val version: $version")
  println(s"val pi: $pi")
  println(s"val isAwesome: $isAwesome")
  println(s"Inferred types - language: $language, year: $year, ratio: $ratio")
  println(s"Mutable counter after increments: $counter")
  println(s"Lazy val (first access):")
  println(s"  $expensiveComputation")
  println(s"Lazy val (second access - no recomputation):")
  println(s"  $expensiveComputation")
  println(s"String interpolation: $greeting")
  println(s"Formatted: $formatted")
  println(s"Raw: $raw")
  println(s"Multi-line: $multiLine")

  // Numeric types
  val byte: Byte = 127
  val short: Short = 32767
  val int: Int = 2147483647
  val long: Long = 9223372036854775807L
  val float: Float = 3.14f
  val double: Double = 3.14159265358979
  val char: Char = 'A'

  println("=== Numeric Types ===")
  println(s"Byte: $byte, Short: $short, Int: $int")
  println(s"Long: $long, Float: $float, Double: $double")
  println(s"Char: $char")
