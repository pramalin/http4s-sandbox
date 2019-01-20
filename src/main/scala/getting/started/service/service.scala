package getting.started.service

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import scala.concurrent.ExecutionContext.Implicits.global
/*
import fs2.{Stream, StreamApp}

object Main extends StreamApp[IO] {
  import cats.implicits._
  import org.http4s.implicits._
  import fs2.StreamApp.ExitCode
  import org.http4s.server.blaze._


  val helloWorldService = HttpService[IO] {
    case GET -> Root / "hello" / name =>
      Ok(s"Hello, $name.")
  }

  case class Tweet(id: Int, message: String)
  implicit def tweetEncoder: EntityEncoder[IO, Tweet] = ???
  implicit def tweetsEncoder: EntityEncoder[IO, Seq[Tweet]] = ???
  def getTweet(tweetId: Int): IO[Tweet] = ???
  def getPopularTweets(): IO[Seq[Tweet]] = ???

  val tweetService = HttpService[IO] {
    case GET -> Root / "tweets" / "popular" =>
      Ok(getPopularTweets())
    case GET -> Root / "tweets" / IntVar(tweetId) =>
      getTweet(tweetId).flatMap(Ok(_))
  }


  val services = tweetService <+> helloWorldService

  val builder = BlazeBuilder[IO].bindHttp(8080, "localhost").mountService(helloWorldService, "/").mountService(services, "/api").start

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] =
    BlazeBuilder[IO]
      .bindHttp(8080, "localhost")
      .mountService(helloWorldService, "/")
      .mountService(services, "/api")
      .serve
}
*/