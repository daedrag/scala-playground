import akka.actor.{Actor, ActorSystem, Props}

object CalcType extends Enumeration {
  type CalcType = Value
  val Sum, Sub, Mul, Div = Value
}
import CalcType._

case class CalcOp[T](first: T, second: T, op: CalcType)
case class CalcResult[T](result: T)

class Calculator extends Actor {
  def receive: Receive = {
    case CalcOp(first: Double, second: Double, op: CalcType) => op match {
      case CalcType.Sum => sender ! CalcResult(first + second)
      case CalcType.Sub => sender ! CalcResult(first - second)
      case CalcType.Mul => sender ! CalcResult(first * second)
      case CalcType.Div => sender ! (if (second != 0) CalcResult(first / second) else new IllegalArgumentException("Second is 0"))
      case _ => sender ! new UnsupportedOperationException("Unknown CalcType")
    }
    case _ => sender ! new UnsupportedOperationException("Unknown message")
  }
}

object PlaygroundApp extends App {
  import akka.pattern.ask
  import akka.util.Timeout
  import scala.concurrent.Await
  import scala.concurrent.duration._

  implicit val timeout = Timeout(5 seconds)

  val system = ActorSystem("ActorSystem")
  val calculator = system.actorOf(Props[Calculator])

  def calculate[T](calcOp: CalcOp[T]) = {
    val future = calculator ? calcOp
    val result = Await.result(future, timeout.duration)
    println(s"${calcOp.toString} => ${result}")
  }

  val random = scala.util.Random
  def nextDouble() = math.floor(random.nextDouble() * 100) / 10
  val calcTypes = Seq(CalcType.Sum, CalcType.Sub, CalcType.Mul, CalcType.Div)

  1 to 10 foreach { n =>
    calculate[Double](CalcOp(nextDouble(), nextDouble(), calcTypes(n % 4)))
  }

  system.terminate()
}