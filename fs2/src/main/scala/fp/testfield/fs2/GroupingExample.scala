package fp.testfield.fs2

import cats._
import cats.implicits._
import cats.effect.{ExitCode, IO, IOApp}

object GroupingExample extends IOApp {
  
  override def run(args: List[String]): IO[ExitCode] = {

    fs2.Stream
      .emits(List(("a", 1), ("a",2), ("a", 1), ("a",2), ("a", 1), ("a",2),("a", 1), ("a",2), ("a", 1), ("a",2), ("b", 1), ("c", 1), ("a", 3), ("a", 1), ("a",2)))
      .groupAdjacentBy(_._1)
      .flatMap { e =>
        fs2.Stream.chunk(e._2)
      }
      .chunkLimit(4)
      .map { e =>
        println(e.toList)
      }
      .covary[IO]
      .compile
      .drain
      .map(_ => ExitCode.Success)
  }
}
