package fp.testfield.http4s.repository
import java.util.UUID
import java.util.concurrent.Executors
import java.util.concurrent.atomic.AtomicInteger

import cats._
import cats.effect.{Async, ConcurrentEffect}
import cats.implicits._
import slick.jdbc.H2Profile
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext

class DatabaseProvider[F[_]](implicit A: Async[F]) {
  import model._

  private implicit val dbExecutionContext: ExecutionContext = {
    val threadFactory = Executors.defaultThreadFactory()
    val idCounter     = new AtomicInteger(0)

    ExecutionContext.fromExecutorService(
      Executors.newFixedThreadPool(
        (Runtime.getRuntime.availableProcessors() * 2) + 1,
        (r: Runnable) => {
          val thread = threadFactory.newThread(r)
          thread.setName(s"slick-database-thread-${idCounter.incrementAndGet()}")
          thread
        }
      )
    )
  }

  private[repository] val db: H2Profile.backend.Database = H2Profile.api.Database.forConfig("h2mem1")

  private[repository] val customers = TableQuery[DbCustomer]

  private[repository] def runAsync[A](action: DBIO[A]): F[A] = {
    import scala.util.{Failure, Success}

    A.async { cb =>
      db.run(action).onComplete {
        case Success(value) => cb(Right(value))
        case Failure(error) => cb(Left(error))
      }
    }
  }

  private[repository] def runStream[A](
    action: StreamingDBIO[_, A]
  )(implicit C: ConcurrentEffect[F]): fs2.Stream[F, A] = {
    import fs2.interop.reactivestreams._

    db.stream(action).toStream.covary[F]
  }
}

object DatabaseProvider {

  def apply[F[_]](implicit A: Async[F]): F[DatabaseProvider[F]] = {
    val dbProvider = new DatabaseProvider[F]
    val actions = DBIO.seq(
      dbProvider.customers.schema.create,
      dbProvider.customers ++= Seq.fill(100000)(
        (UUID.randomUUID(), s"${UUID.randomUUID().toString.replace("-", "")}@test.com")
      )
    )

    dbProvider.runAsync(actions).map(_ => dbProvider)
  }

}
