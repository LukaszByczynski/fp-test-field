package fp.testfield.fs2

import cats.Parallel
import cats.effect._
import cats.implicits._
import fs2.Pull

class ParquetPipe[F[_], P[_]](numParalell: Int = 5)(implicit S: Sync[F], P: Parallel[F, P], T: Timer[F]) {
  /*_*/
  private def go(inputStream: fs2.Stream[F, Record], exporter: Option[Exporter]): fs2.Pull[F, String, Unit] = {
    inputStream.pull.unconsN(numParalell * 10000, true).flatMap {
      case None =>
        val output = exporter match {
          case Some(exp) =>
            fs2.Pull.eval(S.catchNonFatal(exp.close())) >>
              fs2.Pull
                .acquireCancellable(S.delay(new FileOps[F](exp.tag)))(_.close())
                .flatMap(file => fs2.Pull.output1(s"${file.resource.name}.pq").onComplete(file.cancel))
          case None =>
            fs2.Pull.pure(())
        }

        output >> fs2.Pull.done

      case Some((record, tailStream)) =>
        if (exporter.isEmpty) {
          Pull.eval(S.catchNonFatal(new Exporter("all"))).flatMap { exporter =>
            go(fs2.Stream.emits(record.toList) ++ tailStream, Some(exporter))
              .handleErrorWith(err => fs2.Pull.eval(S.catchNonFatal(exporter.close())) >> fs2.Pull.raiseError[F](err))
          }
        } else {
          val z: List[List[Record]] = record.toList.grouped(record.size / numParalell).toList

          def write(a: List[Record]) =
            for {
//            _ <- S.delay(println(Thread.currentThread().getName))
//            _ <- T.sleep(Random.nextInt(10).millis)
              _ <- a.traverse(r => S.delay(exporter.foreach(_.write(r))))
            } yield ()

          fs2.Pull.eval(z.parTraverse(write)) >> go(tailStream, exporter)
        }
    }
  }

  def pipe: fs2.Pipe[F, Record, String] = in => go(in, None).stream
}

/*_*/
object ParquetPipeApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    fs2.Stream
      .range(1, 100000000)
      .map(createRecord)
      .covary[IO]
      .through(new ParquetPipe[IO, IO.Par](4).pipe)
      .evalMap(w => IO.shift *> IO.delay(println(s"${System.nanoTime()} ###### hdfs $w")))
      .compile
      .drain
      .map(_ => ExitCode.Success)
  }

  private def createRecord(i: Int): Record = i match {
    case v if v < 5 => Record("a", Array.fill(1024)(0))
    case v if v < 7 => Record("b", Array.fill(1024)(0))
//    case v if v == 8 => throw new IllegalArgumentException("a")
    case _ => Record("c", Array.fill(1024)(0))
  }
}
