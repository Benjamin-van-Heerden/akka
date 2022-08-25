package section3_AkkaActors

import akka.actor.ActorSystem
import akka.actor.Actor
import akka.actor.Props

object ActorsIntro extends App {
  // part 1 - actor systems
  val actorSystem = ActorSystem("firstActorSystem")
  println(actorSystem.name)

  // part 2 - create actors
  // actors are uniquely identified
  // messages are asyncronous
  // each actor may respond differently to the same message
  // actors are fully encapsulated

  class WordCountActor extends Actor {
    // internal data
    var totalWords = 0
    val name = self.path.name

    // behavior
    def receive: Receive = {
      case message: String =>
        println(s"[($name) word counter] I have received: $message")
        totalWords += message.split(" ").length
        println(s"[($name) word counter] total words: $totalWords")
      case msg =>
        println(s"[($name) word counter] I don't understand ${msg.toString}")
    }
  }

  // part 3 - instantiate our actor
  val wordCounter = actorSystem.actorOf(Props[WordCountActor], "wordCounter")
  val anotherWordCounter =
    actorSystem.actorOf(Props[WordCountActor], "anotherWordCounter")
  // can only communicat with actor via the ActorRef

  // part 4 - communicate with the actor
  wordCounter ! "I am learning Akka and it's pretty damn cool" // the ! ("tell") is the method we are invoking with infix notation
  // it may not be apparent, but this is entirely async
  anotherWordCounter ! "A different message"

  // we cannot interact with the properties of actors, they truly are encapsulated
  // wordCounter.totalWords = 10 !! wont work

  // how do we instantiate actors with constructor arguments?

  class Person(name: String) extends Actor {
    def receive: Receive = {
      case s: String =>
        println(
          s"[person] Hi, my name is $name"
        )
      case _ =>
        println("[person] I don't know what you are talking about")
    }
  }

  object Person {
    def props(name: String) = Props(new Person(name))
  }

  val person =
    actorSystem.actorOf(Person.props("Bob"), "personActor")

  person ! "Say something"

  person ! 56

}
