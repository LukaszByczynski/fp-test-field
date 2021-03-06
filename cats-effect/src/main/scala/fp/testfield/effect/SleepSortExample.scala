package fp.testfield.effect

import cats.Monad
import cats.effect._
import cats.implicits._
import cats.effect.concurrent.Ref
import cats.mtl.MonadState

import scala.concurrent.duration._

object SleepSortExample extends IOApp {

  def sleepSort[F[_]: Monad](input: List[Int])(
    implicit MS: MonadState[F, List[Int]],
    T: Timer[F],
    C: Concurrent[F]
  ): F[List[Int]] = {
    input
      .traverse { i =>
        val fiber = for {
          _ <- T.sleep(i.second)
          _ <- MS.modify(_ :+ i)
        } yield i

        C.start(fiber)
      }
      .flatMap(_.traverse(_.join) >> MS.get)
  }

  /*_*/
  override def run(args: List[Predef.String]): IO[ExitCode] = {

    import com.olegpy.meow.effects._

    val input: List[Int] = (0 to 100).map(_ => List(1, 5, 2, 9)).toList.flatten

    val sortedList = for {
      stateRef <- Ref[IO].of(List[Int]())
      result   <- stateRef.runState(implicit st => sleepSort[IO](input))
    } yield result

    sortedList.flatMap(r => IO.delay(println(r))) >> IO.delay(ExitCode.Success)
  }
}
