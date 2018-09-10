package fp.testfield.cats.effect

import java.util.concurrent.Executors

import cats.effect._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.Random

object TryToFetch100ItemsApp extends IOApp {
  import cats._
  import cats.implicits._

  def randomIO[F[_]](implicit F: Sync[F]): F[String] = F.delay {
    println("[START]: random IO")
    val start = System.currentTimeMillis()
    Thread.sleep(500 + Random.nextInt(600))
    println(s"[STOP]: took ${ System.currentTimeMillis() - start } ms")
    "ok"
  }

  /*_*/
  def customThreadPool[F[_]](implicit F: Sync[F]): Resource[F, ExecutionContext] =
    Resource(F.delay {
      val executor = Executors.newFixedThreadPool(5)
      val ec = ExecutionContext.fromExecutor(executor)
      (ec, F.delay(executor.shutdown()))
    })


  override def run(args: List[String]): IO[ExitCode] = {

    val tasks = List.fill(100)(randomIO[IO].timeout(1.second).attempt)

    val boostrap = customThreadPool[IO].use { ec => tasks.parTraverse(e => contextShift.evalOn(ec)(e).start)
    }
  }

}
