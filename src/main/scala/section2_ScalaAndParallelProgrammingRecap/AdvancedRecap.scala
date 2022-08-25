package section2_ScalaAndParallelProgrammingRecap

import scala.concurrent.Future

object AdvancedRecap extends App {
  // partial functions
  val partialFuntion: PartialFunction[Int, Int] = {
    case 1 => 42
    case 2 => 56
  }

  val pf = (x: Int) =>
    x match {
      case 1 => 42
      case 2 => 56
    }

  val function: (Int => Int) = pf

  val modifiedList = List(1, 2, 3).map {
    case 1 => 42
    case 2 => 56
    case 3 => 78
  }

  // lifting
  val lifted = partialFuntion.lift // total function Int => Option[Int]
  val result = lifted(1) // Some(42)
  println(result)
  val result2 = lifted(5000) // None

  // orElse
  val pfChain = partialFuntion.orElse[Int, Int] { case 4 =>
    42
  }

  val pfChainRes = pfChain(1) // 42
  val pfChainRes2 = pfChain(4) // 42
  // val pfChainRes3 = pfChain(5) // throw a MatchError

  // type aliases
  type RecievePayload = PartialFunction[Any, Unit]

  def recieved: RecievePayload = { case "ping" =>
    println("received ping")
  }

  recieved("ping")

  // implicits
  implicit val timeout = 3000
  def setTimeout(f: () => Unit)(implicit timeout: Int) = f()

  setTimeout(() => println("timeout")) // may omit the extra parameter

  // implicit conversions
  // 1) implicit defs
  case class Person(name: String) {
    def greet = println(s"Hi, my name is $name")
  }

  implicit def fromStringToPerson(string: String): Person = Person(string)

  "Peter".greet
  // fromStringToPerson("Peter").greet -- automatically done by the compiler

  // 2) implicit classes
  implicit class Dog(name: String) {
    def bark = println("bark")
  }

  "Lassie".bark
  // new Dog("Lassie").bark -- automatically done by the compiler

  // implicits may become confusing if we do not organize them properly
  // local scope
  implicit val inverseOrdering: Ordering[Int] = Ordering.fromLessThan(_ > _)
  println(List(1, 2, 3).sorted) // List(3, 2, 1) -- now descending

  // imported scope
  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future {
    println("hello from future")
  }

  // companion objects of the types included in the call
  object Person {
    // alphabetic ordering by name
    implicit val personOrdering: Ordering[Person] =
      Ordering.fromLessThan((a, b) => a.name.compareTo(b.name) < 0)
  }

  println(
    List(Person("Bob"), Person("Alice")).sorted
  ) // List(Person(Alice), Person(Bob))

}
