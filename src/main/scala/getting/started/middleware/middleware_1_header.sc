import cats.data.Kleisli
import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._

def myMiddle(service: HttpRoutes[IO], header: Header): HttpRoutes[IO] = Kleisli { req: Request[IO] =>
  service(req).map {
    case Status.Successful(resp) =>
      resp.putHeaders(header)
    case resp =>
      resp
  }
}

val service = HttpRoutes.of[IO] {
  case GET -> Root / "bad" =>
    BadRequest()
  case _ =>
    Ok()
}

val goodRequest = Request[IO](Method.GET, Uri.uri("/"))
val badRequest = Request[IO](Method.GET, Uri.uri("/bad"))
service.orNotFound(goodRequest).unsafeRunSync
service.orNotFound(badRequest).unsafeRunSync


val wrappedService = myMiddle(service, Header("SomeKey", "SomeValue"));
wrappedService.orNotFound(goodRequest).unsafeRunSync
wrappedService.orNotFound(badRequest).unsafeRunSync


object MyMiddle {
  def addHeader(resp: Response[IO], header: Header) =
    resp match {
      case Status.Successful(resp) => resp.putHeaders(header)
      case resp => resp
    }

  def apply(service: HttpRoutes[IO], header: Header) =
    service.map(addHeader(_, header))
}

val newService = MyMiddle(service, Header("SomeKey", "SomeValue"))
newService.orNotFound(goodRequest).unsafeRunSync
newService.orNotFound(badRequest).unsafeRunSync

// Composing Services with Middleware
val apiService = HttpRoutes.of[IO] {
  case GET -> Root / "api" =>
    Ok()
}

val aggregateService = apiService <+> MyMiddle(service, Header("SomeKey", "SomeValue"))
val apiRequest = Request[IO](Method.GET, Uri.uri("/api"))
aggregateService.orNotFound(goodRequest).unsafeRunSync
aggregateService.orNotFound(apiRequest).unsafeRunSync
