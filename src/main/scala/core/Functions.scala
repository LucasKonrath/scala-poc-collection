package core

@main def functionsPoc(): Unit =
  println("=== Scala Functions POC ===")

  // Basic function
  def add(a: Int, b: Int): Int = a + b
  println(s"add(3, 5) = ${add(3, 5)}")

  // Return type inference
  def multiply(a: Int, b: Int) = a * b
  println(s"multiply(4, 6) = ${multiply(4, 6)}")

  // Unit return type (like void)
  def greet(name: String): Unit = println(s"Hello, $name!")
  greet("Scala")

  // Default parameters
  def power(base: Double, exponent: Int = 2): Double =
    Math.pow(base, exponent)
  println(s"power(3) = ${power(3)}")
  println(s"power(3, 3) = ${power(3, 3)}")

  // Named parameters
  println(s"power(exponent = 4, base = 2) = ${power(exponent = 4, base = 2)}")

  // Multi-line function body
  def factorial(n: Int): Long =
    if n <= 1 then 1L
    else n * factorial(n - 1)
  println(s"factorial(10) = ${factorial(10)}")

  // Tail-recursive function
  def factorialTailRec(n: Int): Long =
    @scala.annotation.tailrec
    def loop(current: Int, acc: Long): Long =
      if current <= 1 then acc
      else loop(current - 1, current * acc)
    loop(n, 1L)
  println(s"factorialTailRec(10) = ${factorialTailRec(10)}")

  // Varargs
  def sum(numbers: Int*): Int = numbers.sum
  println(s"sum(1, 2, 3, 4, 5) = ${sum(1, 2, 3, 4, 5)}")

  // Higher-order functions (functions that take functions)
  def applyTwice(f: Int => Int, x: Int): Int = f(f(x))
  println(s"applyTwice(x => x * 2, 3) = ${applyTwice(x => x * 2, 3)}")

  // Lambda / anonymous functions
  val double = (x: Int) => x * 2
  val square: Int => Int = x => x * x
  println(s"double(5) = ${double(5)}")
  println(s"square(5) = ${square(5)}")

  // Curried functions
  def addCurried(a: Int)(b: Int): Int = a + b
  val add5 = addCurried(5)
  println(s"addCurried(5)(3) = ${addCurried(5)(3)}")
  println(s"add5(3) = ${add5(3)}")

  // Function composition
  val doubleIt: Int => Int = _ * 2
  val addOne: Int => Int = _ + 1
  val doubleAndAddOne = doubleIt.andThen(addOne)
  val addOneAndDouble = doubleIt.compose(addOne)
  println(s"doubleAndAddOne(3) = ${doubleAndAddOne(3)}")   // (3*2)+1 = 7
  println(s"addOneAndDouble(3) = ${addOneAndDouble(3)}")   // (3+1)*2 = 8

  // Partial functions
  val divideByTwo: PartialFunction[Int, Double] =
    case x if x != 0 => x / 2.0
  println(s"divideByTwo.isDefinedAt(4) = ${divideByTwo.isDefinedAt(4)}")
  println(s"divideByTwo.isDefinedAt(0) = ${divideByTwo.isDefinedAt(0)}")
  println(s"divideByTwo(4) = ${divideByTwo(4)}")

  // Using functions with collections
  val numbers = List(1, 2, 3, 4, 5)
  println(s"numbers.map(_ * 2) = ${numbers.map(_ * 2)}")
  println(s"numbers.filter(_ > 3) = ${numbers.filter(_ > 3)}")
  println(s"numbers.reduce(_ + _) = ${numbers.reduce(_ + _)}")
  println(s"numbers.foldLeft(0)(_ + _) = ${numbers.foldLeft(0)(_ + _)}")
