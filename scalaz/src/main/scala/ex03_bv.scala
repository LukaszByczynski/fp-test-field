import scalaz._
import Scalaz._

import zio._
import zio.interop.scalaz72._

package repository {

  trait Repository[Id, T] {
    val repositoryService: Repository.Service[Id, T]
  }

  object Repository {

    trait Service[Id, T] {
      def find(id: Id): Task[Option[T]]
      def save(id: Id, item: T): Task[Unit]

    }
  }
}

package audit {

  trait Audit {
    val auditService: Audit.Service
  }

  object Audit {

    trait Service {
      def audit(msg: String): Task[Unit]
    }
  }
}

object RepositoryInMem {

  def apply[Id, T] = Ref.make(Map[Id, T]()).map { safeMap =>
    new repository.Repository[Id, T] {
      override val repositoryService = new repository.Repository.Service[Id, T] {
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

object auditor {

  def apply() = new audit.Audit {
    override val auditService = new audit.Audit.Service() {
      override def audit(msg: String): Task[Unit] = {
        Task(println(s"auditor: $msg"))
      }
    }
  }
}

object programs {
  import audit._
  import repository._

  type Env         = Repository[Int, String] with Audit
  type AppStack[A] = ZIO[Env, Throwable, A]

  def findWithAudit(id: Int): AppStack[Option[String]] = {
    for {
      _   <- ZIO.accessM[Env](_.auditService.audit("query to db"))
      res <- ZIO.accessM[Env](_.repositoryService.find(id))
      _   <- ZIO.accessM[Env](_.auditService.audit(s"result $res"))
    } yield res
  }
}

/*_*/
object ex03_bv extends App {

  import audit._
  import repository._

  override def run(args: List[String]) = {
    val program = RepositoryInMem[Int, String].flatMap { repo =>
      val audit = auditor()
      val rtm = new Repository[Int, String] with Audit {
        override val repositoryService = repo.repositoryService
        override val auditService      = audit.auditService
      }

      for {
        r <- programs.findWithAudit(2).provide(rtm)
        // _ <- repo.repository.save(2, "aaa")
        // r <- repo.repository.find(2)
        _ <- Task(println(r))
      } yield r
    }

    program.fold(_ => -1, _ => 0)
  }
}
