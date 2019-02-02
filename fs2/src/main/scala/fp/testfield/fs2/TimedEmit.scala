package fp.testfield.fs2

import fs2._
import cats.implicits._
import cats.effect.{ExitCode, IO, IOApp}

import scala.util.Random
import scala.concurrent.duration._

object TimedEmit extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    Stream
      .range(1, 100)
      .evalMap(i => IO.sleep(Random.nextInt(100).milli) *> IO.pure(i))
      .groupWithin(10, 80.milli)
      .evalMap(r => IO(println(r)))
      .compile
      .drain
      .as(ExitCode.Success)
  }
}
