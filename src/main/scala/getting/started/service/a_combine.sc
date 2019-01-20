import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import scala.concurrent.ExecutionContext.Implicits.global

import cats.implicits._

import org.http4s.server.blaze._
import org.http4s.implicits._


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
