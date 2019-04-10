package fp.testfield.effect

import java.util.concurrent.Executors

import cats._
import cats.implicits._
import cats.effect.{ExitCode, IO, IOApp}

import scala.concurrent.ExecutionContext

object IOParallelExample extends IOApp {

  private val cs = IO.contextShift(ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10)))

  override def run(args: List[String]): IO[ExitCode] = {

    val list: List[IO[Unit]] = (0 to 10).map { n =>
      IO.shift(cs) *> IO.delay {
        println(s"${System.currentTimeMillis()} ${Thread.currentThread().getId} start item $n")
        Thread.sleep((10 - n) * 1000)
        println(s"${System.currentTimeMillis()} ${Thread.currentThread().getId} stop item $n")
      }
    }.toList

    def runAction[F[_]: Monad, G[_], A](list: List[F[A]])(implicit P: Parallel[F, G]) =
      list.parSequence

    runAction[IO, IO.Par, Unit](list)
      .flatMap(x => IO.delay(println(x)))
      .as(ExitCode.Success)

  }
}
