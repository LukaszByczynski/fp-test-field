package fp.testfield.effect

import cats.effect._
import cats.implicits._
import fs2.Pull

final case class Record(tag: String, data: Array[Int])

final class Exporter(val tag: String) {
  println(s"${System.nanoTime()} new exporter $tag")

  def close(): Unit = {
    println(s"${System.nanoTime()} close exporter $tag")
  }

  def write(record: Record): Unit = {
    println(s"${System.nanoTime()} w $tag")
  }
}

final class FileOps[F[_]](val name: String)(implicit S: Sync[F]) {
  println(s"${System.nanoTime()} Open file: $name")

  def close(): F[Unit] = {
    S.delay(println(s"${System.nanoTime()} close file $name"))
  }
}

class PartitionPipe[F[_]](implicit S: Sync[F]) {

  private def isTagChange(state: Option[Exporter], tag: String): Boolean = {
    state.isEmpty || state.exists(_.tag != tag)
  }

  /*_*/
  private def go(inputStream: fs2.Stream[F, Record], exporter: Option[Exporter]): fs2.Pull[F, String, Unit] = {
    def closeExporter = fs2.Pull.eval(S.delay(exporter.foreach(_.close())))

    def tryPushResultAndReleaseExporter = exporter match {
      case Some(st) =>
        val pushResult = fs2.Pull
          .acquireCancellable(S.delay(new FileOps[F](st.tag)))(_.close())
          .flatMap(file => fs2.Pull.output1(s"${file.resource.name}.pq").onComplete(file.cancel))

        closeExporter >> pushResult
      case _ =>
        fs2.Pull.pure(())
    }

    inputStream.pull.uncons1.flatMap {
      case None => tryPushResultAndReleaseExporter >> fs2.Pull.done
      case Some((record, tailStream)) =>
        def writeRecord = (state: Option[Exporter]) => fs2.Pull.eval(S.delay(state.foreach(_.write(record))))

        if (isTagChange(exporter, record.tag)) {
          val createExporter = fs2.Pull.acquire(S.delay(Some(new Exporter(record.tag))))(st => S.delay(st.foreach(_.close())))

          tryPushResultAndReleaseExporter >>  createExporter.flatMap { newExporter =>
            writeRecord(newExporter) >> go(tailStream, newExporter)
          }
        } else {
          writeRecord(exporter) >> go(tailStream, exporter)
        }
    }
  }

  def pipe: fs2.Pipe[F, Record, String] = in => go(in, None).streamNoScope
}

object PartitionExample extends IOApp {

  /*_*/
  override def run(args: List[String]): IO[ExitCode] = {
    fs2.Stream
      .range(1, 10)
      .map(createRecord)
      .covary[IO]
      .through(new PartitionPipe[IO].pipe)
      .evalMap(w => IO.delay(println(s"${System.nanoTime()}  hdfs $w")))
      .compile
      .drain
      .map(_ => ExitCode.Success)
  }

  private def createRecord(i: Int): Record = i match {
    case v if v < 5 => Record("a", Array.fill(1024)(0))
    case v if v < 7 => Record("b", Array.fill(1024)(0))
    case v if v == 8 => throw new IllegalArgumentException("a")
    case _ => Record("c", Array.fill(1024)(0))
  }
}
