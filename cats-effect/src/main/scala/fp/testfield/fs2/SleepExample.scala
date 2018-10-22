package fp.testfield.fs2

import cats.effect._
import cats.implicits._

trait Ufo[F[_]] {

  def sleep(): F[Unit]

  def save(): F[Unit]

}

object Ufo {

  def apply[F[_]](implicit T: Timer[F], S: Sync[F]): Ufo[F] = new Ufo[F] {

    override def sleep(): F[Unit] = {
      import scala.concurrent.duration._

      for {
        _ <- S.delay(println("sleep start"))
        _ <- T.sleep(5.seconds)
        _ <- S.delay(println("sleep end"))
      } yield ()
    }

    override def save(): F[Unit] = Sync[F].delay(println("saved"))
  }
}

object SleepExample extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {
    val ufo = Ufo[IO]

    for {
      _ <- ufo.sleep()
      _ <- ufo.save()
    } yield ExitCode.Success
  }
}
