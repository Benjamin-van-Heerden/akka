package section2_ScalaAndParallelProgrammingRecap

import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure

object MultiThreadingRecap extends App {
  // creating threads on the jvm

  val aThread = new Thread(() => println("I am running in parallel"))

  aThread.start()

  aThread.join() // blocks the main thread until aThread finishes

  // threads are unpredictable
  val threadHello = new Thread(() => (1 to 10).foreach(_ => println("hello")))
  val threadGoodbye = new Thread(() =>
    (1 to 10).foreach(_ => println("goodbye"))
  )

  threadHello.start()
  threadGoodbye.start()

  // different runs produce different results! lots of problems

  class BankAccount(private var amount: Int) {
    override def toString: String = "" + amount
    def withdraw(money: Int) = this.amount -= money
    def safeWithdraw(money: Int) = this.synchronized {
      withdraw(money)
    }
  }

  /*
  BA(1000)
  T1 -> withdraw(100)
  T2 -> withdraw(200)

  T1 -> this.amount - ... // PREEMPTED by OS
  T2 -> this.amount - 200 = 800
  T1 -> this.amount - 100 = 900

  => result = 900!

  this.amount is NOT ATOMIC

  the problem of synchronising threads becomes very difficult for larger applications

   */

  // inter-thread communication on the JVM
  // wait - notify mechanism

  // Scala Futures
  import scala.concurrent.ExecutionContext.Implicits.global
  val future = Future {
    // long computation
    42
  }

  future.onComplete {
    case Success(value) => println(value)
    case Failure(ex)    => ex.printStackTrace()
  }

  // future has functional primitives
  val aProcessedFuture = future.map(_ + 1) // Future[Int]
  val aFlatFuture = future.flatMap(x => Future(x + 2)) // Future[Int]
  val filteredFuture =
    future.filter(_ % 2 == 0) // Future[Int] or NoSuchElementException

  // for comprehensions
  val aNonsenseFuture = for {
    meaningOfLife <- future
    filteredMeaning <- filteredFuture
  } yield meaningOfLife + filteredMeaning

  // other userful functions
  // andThen, recover, recoverWith, and so on

  // Promises

}
