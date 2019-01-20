import cats._, cats.effect._, cats.implicits._, cats.data._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._
import org.http4s.server._

case class User(id: Long, name: String)


val authUser1: Kleisli[OptionT[IO, ?], Request[IO], User] =
  Kleisli(_ => OptionT.liftF(IO(???)))

val middleware1: AuthMiddleware[IO, User] =
  AuthMiddleware(authUser1)


val authedService1: AuthedService[User, IO] =
  AuthedService {
    case GET -> Root / "welcome" as user => Ok(s"Welcome, ${user.name}")
  }

val service1: HttpRoutes[IO] = middleware1(authedService1)

val authUser2: Kleisli[IO, Request[IO], Either[String,User]] = Kleisli(_ => IO(???))

val onFailure: AuthedService[String, IO] = Kleisli(req => OptionT.liftF(Forbidden(req.authInfo)))
val middleware2 = AuthMiddleware(authUser2, onFailure)

val service2: HttpRoutes[IO] = middleware2(authedService2)

