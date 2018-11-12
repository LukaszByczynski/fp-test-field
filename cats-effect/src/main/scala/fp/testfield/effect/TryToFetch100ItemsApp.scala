package fp.testfield.effect

import java.util.concurrent.Executors

import cats.effect._

import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import scala.util.Random

object TryToFetch100ItemsApp extends IOApp {

  import cats.implicits._

  val executor = Executors.newFixedThreadPool(8)
  val ec       = ExecutionContext.fromExecutor(executor)

  def randomIo: IO[String] =
    for {
      _         <- IO.shift(ec)
      _         <- IO.delay(println("[START]: random IO"))
      startTime <- IO.delay(System.currentTimeMillis())
      _         <- IO(Thread.sleep(500 + Random.nextInt(600)))
      _         <- IO.delay(println(s"[STOP]: took ${System.currentTimeMillis() - startTime} ms"))
    } yield "ok"

  override def run(args: List[String]): IO[ExitCode] = {
    val program = for {
      startTime <- IO.delay(System.currentTimeMillis())
      tasks     <- IO(List.fill(100)(randomIo.timeout(1.second).attempt))
      fibers    <- tasks.parTraverse(_.start)
      result    <- fibers.parTraverse(_.join)
      _ <- IO.delay {
        println(s"[APP]: took ${System.currentTimeMillis() - startTime} ms")
      }
    } yield result

    program.timeout(2.second).map { r =>
      println(r)
      println(s"Right: ${r.count(_.isRight)}")
      println(s"Left: ${r.count(_.isLeft)}")
      executor.shutdown()
      ExitCode.Success
    }
  }

}
