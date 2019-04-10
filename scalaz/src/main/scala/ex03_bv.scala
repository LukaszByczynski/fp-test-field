import scalaz._
import Scalaz._

import zio._
import zio.interop.scalaz72._

package repository {

  trait Repository[Id, T] {
    val service: Repository.Service[Id, T]
  }

  object Repository {

    trait Service[Id, T] {
      def find(id: Id): Task[Option[T]]
      def save(id: Id, item: T): Task[Unit]

    }
  }

  object RepositoryInMem {

    def apply[Id, T] = Ref.make(Map[Id, T]()).map { safeMap =>
      new Repository[Id, T] {
        override val service = new Repository.Service[Id, T] {
          override def find(id: Id): Task[Option[T]] = {
            safeMap.get.map(_.get(id))
          }

          override def save(id: Id, item: T): Task[Unit] = {
            safeMap.update(_ + (id -> item)) >> Task.unit
          }
        }
      }
    }
  }
}

object database {
  import repository.Repository

  def find[Id, T](id: Id): ZIO[Repository[Id, T], Throwable, Option[T]] = {
    ZIO.accessM(_.service.find(id))
  }
}

/*_*/
object ex03_bv extends App {
  import repository._

  override def run(args: List[String]) = {
    val program = RepositoryInMem[Int, String].flatMap { repo =>
      for {
        r <- database.find(2).provide(repo)
        // _ <- repo.repository.save(2, "aaa")
        // r <- repo.repository.find(2)
        _ <- Task(println(r))
      } yield r
    }

    program.fold(_ => -1, _ => 0)
  }
}
