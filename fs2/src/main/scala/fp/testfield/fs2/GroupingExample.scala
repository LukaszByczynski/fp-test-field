package fp.testfield.fs2

import cats.effect.concurrent.Ref
import cats.effect.{ExitCode, IO, IOApp}
import cats.implicits._

class Exporter(tag: String) {
  println(s"new exporter $tag")

  def close(): Unit = {
    println(s"close $tag")
  }
}

case class State(tag: String, exporter: Exporter)

object GroupingExample extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    def releaseExporter(state: Option[State]): Unit = state.foreach(_.exporter.close())

    def createRecord(i: Int): (String, Array[Int]) = i match {
      case v if v < 50000 => ("a", Array.fill(1024)(0))
      case v if v < 70000 => ("b", Array.fill(1024)(0))
      case v if v == 80000 => throw new IllegalArgumentException("a")
      case _ => ("c", Array.fill(1024)(0))
    }

    def createNewAndWriteToExporter(
      streamState: Ref[IO, Option[State]],
      tag: String,
      item: Array[Int]
    ): IO[Array[Int]] =
      streamState.modify { oldState =>
        releaseExporter(oldState)
        (Some(State(tag, new Exporter(tag))), item)
      }

    def writeToExporter(state: Ref[IO, Option[State]], item: Array[Int]): IO[Array[Int]] =
      state.get.map(_ => item)

    def reuseExporter(state: Option[State], tag: String): Boolean =
      state.exists(_.tag == tag)

    fs2.Stream
      .bracket(Ref.of[IO, Option[State]](None))(_.getAndSet(None).map(releaseExporter))
      .flatMap { streamState =>
        fs2.Stream
          .range(1, 100000)
          .map(createRecord)
          .covary[IO]
          .evalMap {
            case (tag, item) =>
              streamState.get.flatMap { state =>
                if (reuseExporter(state, tag))
                  writeToExporter(streamState, item)
                else
                  createNewAndWriteToExporter(streamState, tag, item)
              }
          }
      }
      .compile
      .drain
      .map(_ => ExitCode.Success)
  }
}
