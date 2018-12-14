package fp.testfield.effect

import cats._
import cats.effect.{ExitCode, IO, IOApp, Sync}
import cats.implicits._

object IOParallelExample extends IOApp {
  override def run(args: List[String]): IO[ExitCode] = {

    def list[F[_]: Sync] = List(
      Sync[F].pure("t1"),
      Sync[F].pure("t2"),
      Sync[F].pure("t3")
    )

    def runAction[F[_]: Monad, G[_]](list: List[F[String]])(implicit P: Parallel[F, G]) =
      list.parSequence

    runAction[IO, IO.Par](list[IO])
      .flatMap(x => IO.delay(println(x)))
      .as(ExitCode.Success)

  }
}
