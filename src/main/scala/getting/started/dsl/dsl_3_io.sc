import cats.effect._
import org.http4s._, org.http4s.dsl.io._, org.http4s.implicits._

val io = Ok(IO {
     println("I run when the IO is run.")
     "Mission accomplished!"
})

io.unsafeRunSync