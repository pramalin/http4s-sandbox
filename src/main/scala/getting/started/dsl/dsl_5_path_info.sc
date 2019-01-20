/*
 Matching and extracting requests
 A Request is a regular case class - you can destructure it to extract its values. By extension,
 you can also match/case it with different possible destructurings. To build these different
 extractors, you can make use of the DSL.

 The -> object
 More often, you extract the Request into a HTTP Method and path info via the -> object. On
 the left side is the method, and on the right side, the path info. The following matches a
 request to GET /hello:
 */

import cats.effect._
import org.http4s._
import org.http4s.dsl.io._
import org.http4s.implicits._

HttpService[IO] {
  case GET -> Root / "hello" => Ok("hello")
}

HttpService[IO] {
  case GET -> Root => Ok("root")
}

HttpService[IO] {
  case GET -> Root / "hello" / name => Ok(s"Hello, $name!")
}


import java.time.LocalDate
import scala.util.Try
import org.http4s.client.dsl.io._

object LocalDateVar {
  def unapply(str: String): Option[LocalDate] = {
    if (!str.isEmpty)
      Try(LocalDate.parse(str)).toOption
    else
      None
  }
}
def getTemperatureForecast(date: LocalDate): IO[Double] = IO(42.23)

val dailyWeatherService = HttpService[IO] {
  case GET -> Root / "weather" / "temperature" / LocalDateVar(localDate) =>
    Ok(getTemperatureForecast(localDate).map(s"The temperature on $localDate will be: " + _))
}

 //println(GET(Uri.uri("/weather/temperature/2016-11-05")).flatMap(dailyWeatherService.orNotFound(_)).unsafeRunSync)
println(GET(Uri.uri("/weather/temperature/2016-11-05")).flatMap(dailyWeatherService.apply(_).getOrElseF(NotFound())).unsafeRunSync)
