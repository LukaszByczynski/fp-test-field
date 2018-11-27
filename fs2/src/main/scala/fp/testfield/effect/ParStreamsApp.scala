package fp.testfield.effect

import cats.effect._
import cats.implicits._
import fs2._
import fs2.concurrent._

import scala.concurrent.duration._
import scala.util.Random

object ParStreamsApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    def setupProcesssors[F[_]](inputQueue: Queue[F, Int], outputQueue: Queue[F, Int], parallel: Int)(implicit F: Concurrent[F], T: Timer[F])  = {

      def createProcessor(latency: FiniteDuration): Pipe[F, Int, Unit] = in => {
        in.evalMap { n =>
          F.delay(println(s"${Thread.currentThread().getName} Pulling out $n from Queue")) >> F.point(n)
        }.fold1(_ + _).to(outputQueue.enqueue)
      }

      val processors = (1 to parallel).map(_ => inputQueue.dequeue.through(createProcessor(Random.nextInt(1000).millis)))
      Stream.emits(processors).parJoin(parallel)
    }

    val stream = for {
      inputQueue <- Stream.eval(Queue.bounded[IO, Int](5))
      outputQueue <- Stream.eval(Queue.bounded[IO, Int](10))
      result <- (Stream.range(0, 10).covary[IO].to(inputQueue.enqueue) concurrently setupProcesssors(inputQueue, outputQueue, 5)
      result <- outputQueue.dequeue
    } yield result

    stream.compile.toList.flatMap(r => IO.delay(println(r))) >> IO.pure(ExitCode.Success)

  }
}
