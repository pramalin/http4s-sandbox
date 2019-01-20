import cats.effect._
import org.http4s._, org.http4s.dsl.io._, org.http4s.implicits._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

val io = Ok(IO.fromFuture(IO(Future {
  println("I run when the future is constructed.")
  "Greetings from the future!"
})))

io.unsafeRunSync