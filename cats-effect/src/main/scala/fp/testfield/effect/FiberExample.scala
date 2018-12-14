package fp.testfield.effect
import cats.effect.{ExitCode, IO, IOApp}

/*_*/
object FiberExample extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    val p1 = IO.delay(println("aa"))

    val z: IO[Unit] = for {
      fiber1 <- p1.start
      fiber2 <- p1.start
      _      <- fiber1.join
      _      <- fiber2.join
    } yield ()

    z.attempt.map(_ => ExitCode.Success)

  }
}
