//package fp.testfield.fs2
//
//import cats.effect._
//import cats.implicits._
//import fs2.Pull
//
//class ParquetPipe[F[_]](implicit S: Sync[F]) {
//
//  /*_*/
//  private def go(inputStream: fs2.Stream[F, Record], exporter: Option[Exporter]): fs2.Pull[F, String, Unit] = {
//    inputStream.pull.uncons1.flatMap {
//      case None =>
//        val output = exporter match {
//          case Some(exp) =>
//            fs2.Pull.eval(S.catchNonFatal(exp.close())) >>
//              fs2.Pull
//                .acquireCancellable(S.delay(new FileOps[F](exp.tag)))(_.close())
//                .flatMap(file => fs2.Pull.output1(s"${file.resource.name}.pq").onComplete(file.cancel))
//          case None =>
//            fs2.Pull.pure(())
//        }
//
//        output >> fs2.Pull.done
//
//      case Some((record, tailStream)) =>
//        if (exporter.isEmpty) {
//          Pull.eval(S.catchNonFatal(new Exporter("all"))).flatMap { exporter =>
//            go(fs2.Stream(record) ++ tailStream, Some(exporter))
//              .handleErrorWith(err => fs2.Pull.eval(S.catchNonFatal(exporter.close())) >> fs2.Pull.raiseError[F](err))
//          }
//        } else {
//          fs2.Pull.eval(S.catchNonFatal(exporter.foreach(_.write(record)))) >> go(tailStream, exporter)
//        }
//    }
//  }
//
//  def pipe: fs2.Pipe[F, Record, String] = in => go(in, None).stream
//}
//
//object ParquetPipeApp extends IOApp {
//
//  /*_*/
//  override def run(args: List[String]): IO[ExitCode] = {
//    fs2.Stream
//      .range(1, 10)
//      .map(createRecord)
//      .covary[IO]
//      .through(new ParquetPipe[IO].pipe)
//      .evalMap(w => IO.delay(println(s"${System.nanoTime()}  hdfs $w")))
//      .compile
//      .drain
//      .map(_ => ExitCode.Success)
//  }
//
//  private def createRecord(i: Int): Record = i match {
//    case v if v < 5 => Record("a", Array.fill(1024)(0))
//    case v if v < 7 => Record("b", Array.fill(1024)(0))
////    case v if v == 8 => throw new IllegalArgumentException("a")
//    case _ => Record("c", Array.fill(1024)(0))
//  }
//}
