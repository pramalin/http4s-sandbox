import cats.effect._

import scala.concurrent.ExecutionContext
import java.util.concurrent.Executors
import org.http4s._, org.http4s.dsl.io._, org.http4s.implicits._


/*
 Streaming bodies
 Streaming bodies are supported by returning a fs2.Stream. Like Futures and IOs, the stream may be
 of any type that has an EntityEncoder.

 An intro to Stream is out of scope, but we can glimpse the power here. This stream emits the elapsed
 time every 100 milliseconds for one second:
 */
implicit val timer: Timer[IO] = IO.timer(scala.concurrent.ExecutionContext.Implicits.global)
val clientsThreadPool = Executors.newCachedThreadPool()
implicit val clientsExecutionContext = ExecutionContext.fromExecutor(clientsThreadPool)

val drip: fs2.Stream[IO, String] =
     fs2.Scheduler[IO](2).flatMap { s =>
          import scala.concurrent.duration._
          s.awakeEvery[IO](100.millis).map(_.toString).take(10)
     }

val dripOutIO = drip.through(fs2.text.lines).through(_.evalMap(s => {IO{println(s); s}})).compile.drain

dripOutIO.unsafeRunSync


Ok(drip).unsafeRunSync