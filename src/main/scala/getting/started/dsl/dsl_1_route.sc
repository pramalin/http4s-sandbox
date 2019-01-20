
import cats.effect._
import org.http4s._, org.http4s.dsl.io._, org.http4s.implicits._

val service = HttpService[IO] {
    case _ =>
      IO(Response(Status.Ok))
}

val getRoot = Request[IO](Method.GET, uri("/"))

