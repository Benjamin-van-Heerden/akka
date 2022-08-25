package section3_AkkaActors

import akka.actor.Actor
import akka.actor.ActorSystem
import akka.actor.Props
import akka.actor.ActorRef

object ActorCapabilities extends App {
  val actorSystem = ActorSystem("actorCapabilities")

  class SimpleActor extends Actor {
    def receive: Receive = {
      case "Hi"        => sender() ! "Hello there"
      case msg: String => println(s"[$self] I have received: $msg")
      case num: Int    => println(s"[$self] I have received: $num")
      case SpecialMessage(contents) =>
        println(s"[$self] I have received: $contents")
      case SendSelfMessage(content) => self ! content
      case SayHiTo(ref)             => ref ! "Hi"
      case ForwardMessage(ref, message) =>
        ref forward (message + " --Forwarded") // keep the original sender
      case _ => println("[$self] I have received something else")
    }
  }

  val simpleActor = actorSystem.actorOf(Props[SimpleActor], "simpleActor")

  simpleActor ! "hello, actor"

  // 1 - messages can be of any type
  // under two conditions
  // a) messages are IMMUTABLE
  // b) messages are SERIALIZABLE
  simpleActor ! 42

  // in practice use case classes and case objects
  case class SpecialMessage(contents: String)
  simpleActor ! SpecialMessage("I am special")

  // 2 - actors have information about their context and about themselves
  // context.self === `this` in OO speak (or just self)

  case class SendSelfMessage(message: String)

  simpleActor ! SendSelfMessage("hello, myself")

  // 3 - actors can REPLY to messages
  val alice = actorSystem.actorOf(Props[SimpleActor], "alice")
  val bob = actorSystem.actorOf(Props[SimpleActor], "bob")

  case class SayHiTo(ref: ActorRef)

  alice ! SayHiTo(bob)

  // if there is no sender the message will go to the "dead letters"
  // alice ! "Hi" // reply to "me"

  // 5 - forward messages
  // forwarding = sending a message to another actor without changing the ORIGINAL sender

  case class ForwardMessage(ref: ActorRef, message: String)

  alice ! ForwardMessage(bob, "I am being forwarded")
}
