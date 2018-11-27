package fp.testfield.effect

import cats.effect._
import cats.implicits._
import fs2._
import fs2.concurrent._

import scala.concurrent.duration._
import scala.util.Random

object ParStreamsApp extends IOApp {

  override def run(args: List[String]): IO[ExitCode] = {

    def createProcessor[F[_]](latency: FiniteDuration)(implicit F: Concurrent[F], T: Timer[F]): Pipe[F, Int, Int] = in => {
      in.evalMap { n =>
        T.sleep(latency) >> F.delay(println(s"${Thread.currentThread().getName} Pulling out $n from Queue")) >> F.point(n)
      } //.to(outputQueue.enqueue)
    }

    def setupProcesssors[F[_]](inputQueue: Queue[F, Int], outputQueue: Queue[F, Int], parallel: Int)(
      implicit F: Concurrent[F], T: Timer[F]
    ) = {

      def createProcessor(latency: FiniteDuration): Pipe[F, Int, Int] = in => {
        in.evalMap { n =>
          T.sleep(latency) >> F.delay(println(s"${Thread.currentThread().getName} Pulling out $n from Queue")) >> F.point(n)
        } //.to(outputQueue.enqueue)
      }

//      val processors =
//        (0 to parallel).map(_ => inputQueue.dequeue.through(createProcessor(Random.nextInt(1000).millis)))
//      Stream.emits(processors).parJoin(parallel)

      inputQueue.dequeue.through(createProcessor(0.second))
    }

    val stream = for {
      inputQueue <- Stream.eval(Queue.bounded[IO, Int](4))
      _ <- Stream.range(0, 10).covary[IO].to(inputQueue.enqueue) concurrently
        Stream(
          inputQueue.dequeue.through(createProcessor(0.second)).drain,
          inputQueue.dequeue.through(createProcessor(0.second)).drain
        ).parJoin(2)
//      outputQueue <- Stream.eval(Queue.unbounded[IO, Int])
//      result <- Stream(
//        setupProcesssors(inputQueue, null, 1)
//      ).parJoin(1000)
//        setupProcesssors(inputQueue, null, 1))
//      result <- outputQueue.dequeue
    } yield ()

    stream.compile.drain.as(ExitCode.Success)

//      .drain.flatMap(r => IO.delay(println(1)))

  }
}
