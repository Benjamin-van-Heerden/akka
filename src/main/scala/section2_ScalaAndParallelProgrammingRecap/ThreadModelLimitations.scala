package section2_ScalaAndParallelProgrammingRecap

import scala.concurrent.Future

object ThreadModelLimitations extends App {
  // Thread model failings

  // 1) OOP encapsulation is only valid in the single threaded model

  class BankAccount(private var amount: Int) {
    override def toString: String = "" + amount
    def withdraw(money: Int) = this.amount -= money
    def deposit(money: Int) = this.amount += money
    def getAmount = this.amount
  }

  val account = new BankAccount(2000)

  // for (_ <- 1 to 1000) {
  //   new Thread(() => account.withdraw(1)).start()
  // }

  // for (_ <- 1 to 1000) {
  //   new Thread(() => account.deposit(1)).start()
  // }

  // println(account.getAmount) // WTF??

  // OOP Encapsulation is broken in the multi-threaded model
  // can solve with synchronization
  // they introduce their own problems, deadlocks, livelocks, etc.

  // 2) Delegating something to a thread is a PAIN
  // you have a running thread and want to pass a runnable to that thread

  var task: Runnable = null
  val runningThread: Thread = new Thread(() => {
    while (true) {
      while (task == null) {
        runningThread.synchronized {
          println("[background] Waiting for a task...")
          runningThread.wait()
        }
      }

      task.synchronized {
        println("[background] I have a task!")
        task.run()
        task = null
      }
    }
  })

  def delegateToBackgroundThread(r: Runnable) = {
    if (task == null) task = r
    runningThread.synchronized {
      runningThread.notify()
    }
  }

  runningThread.start()
  Thread.sleep(500)
  delegateToBackgroundThread(() => println("Hello, Scala!"))
  Thread.sleep(1000)
  delegateToBackgroundThread(() => println("This should run in the background"))

  // this can quickly become a massive headache in larger applications

  // 3) Tracing and dealing with errors sucks balls

  // 1M numbers in between 10 threads
  import scala.concurrent.ExecutionContext.Implicits.global

  val futures = (1 to 9)
    .map(i => 100000 * i until 100000 * (i + 1))
    .map(range =>
      Future {
        if (range.contains(543261)) throw new RuntimeException("Invalid number")
        range.sum
      }
    )

  val sumFuture =
    Future.reduceLeft(futures)(_ + _) // Future with the sum of all the numbers

  sumFuture.onComplete(println) // where did this error occur and why?
  // what if a thread throws an error that kills the entire JVM

}
