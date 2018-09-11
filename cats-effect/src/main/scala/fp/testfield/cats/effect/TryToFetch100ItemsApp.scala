package fp.testfield.cats.effect

import java.util.concurrent.Executors

import cats.effect._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.Random

object TryToFetch100ItemsApp extends IOApp {
  import cats._
  import cats.implicits._

  def randomIO[F[_] : Monad](implicit F: Timer[F], S : Sync[F]): F[String] = {
    for {
      _ <- S.delay {
        println("[START]: random IO")
      }
      startTime <- S.delay(System.currentTimeMillis())
      _ <- F.sleep((500 + Random.nextInt(600)).millis)
      _ <- S.delay {
        println(s"[STOP]: took ${System.currentTimeMillis() - startTime} ms")
      }
    } yield "ok"
  }

  /*_*/
  def customThreadPool[F[_]](implicit F: Sync[F]): Resource[F, ExecutionContext] =
    Resource(F.delay {
      val executor = Executors.newFixedThreadPool(5)
      val ec = ExecutionContext.fromExecutor(executor)
      (ec, F.delay(executor.shutdown()))
    })


  override def run(args: List[String]): IO[ExitCode] = {

    val tasks = List.fill(100)(randomIO[IO].timeout(950.millis).attempt)

    val program = for {
      startTime <- IO.delay(System.currentTimeMillis())
      fibers <- tasks.traverse(_.start)
      result <- fibers.traverse(_.join)
      _ <- IO.delay {
        println(s"[APP]: took ${System.currentTimeMillis() - startTime} ms")
      }
    } yield result

    program.timeout(1.second).map { r =>
      println(r)
      ExitCode.Success
    }
  }

}
