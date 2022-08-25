package section2_ScalaAndParallelProgrammingRecap

import scala.util.Try

object GeneralRecap extends App {
  val aCondition: Boolean = false
  var aVariable = 42
  aVariable += 1
  println(aVariable)

  // expressions
  val aConditionedVal = if (aCondition) 42 else 43

  // code blocks
  val aCodeBlock = {
    if (aCondition) 74
    56
  }

  // types
  // Unit
  val theUnit = println("Hello, Scala")

  def aFuntion(s: Int) = s + 1

  // recursion - TAIL recursion
  def factorial(n: Int, acc: Int): Int = {
    if (n == 1) acc
    else factorial(n - 1, n * acc)
  }

  println(factorial(5, 1))

  // OOP

  class Animal {
    override def toString(): String = "Animal"
  }
  class Dog extends Animal
  val aDog: Animal = new Dog

  trait Carnivore {
    def eat(a: Animal): Unit
  }

  class Croc extends Animal with Carnivore {
    override def eat(a: Animal): Unit = println(s"$a crunch!")
  }

  // method notations
  val aCroc = new Croc

  // infix notation
  aCroc eat aDog

  val aCarnivore = new Carnivore {
    override def eat(a: Animal): Unit = println(s"$a eats a meat")
  }

  aCarnivore eat aDog

  // generics
  abstract class MyList[+A]
  // companion objects
  object MyList

  // case classes
  case class Person(name: String, age: Int) // a LOT in this course!

  // exceptions
  val aFailure =
    try {
      println("This is a failure")
      throw new NullPointerException // Nothing
    } catch {
      case e: Exception => println("I handled it")
    } finally {
      // side effects
      println("Hello")
    }

  // funtional programming
  val incrementer = new Function1[Int, Int] {
    override def apply(v1: Int): Int = v1 + 1
  }

  println(incrementer(1))

  // syntax sugar
  val anonIncrementer = (x: Int) => x + 1
  println(anonIncrementer(1))

  println(List(1, 2, 3).map(anonIncrementer))
  // map = HOF

  // for comps
  val pairs = for {
    num <- List(1, 2, 3)
    char <- List('a', 'b', 'c')
  } yield (num, s"$char")

  println(pairs)

  // translates to:
  // List(1, 2, 3, 4).flatMap(num => List('a', 'b', 'c').map(char => (num, char)))

  println(
    List(1, 2, 3, 4).flatMap(num =>
      List('a', 'b', 'c').map(char => (num, char))
    )
  )

  // collections
  // Seq, Array, List, Vector, Map, Set

  // Options and Try
  val anOption: Option[Int] = Some(2)
  val aTry = Try {
    throw new RuntimeException
  }

  // pattern matching
  val unknown: Any = 2
  val order = unknown match {
    case 1 => "first"
    case 2 => "second"
    case _ => "unknown"
  }

  val bob = Person("Bob", 22)
  val greeting = bob match {
    case Person(n, _) => s"Hi, my name is $n"
    case _            => "I don't know who I am"
  }
  println(greeting)

}
